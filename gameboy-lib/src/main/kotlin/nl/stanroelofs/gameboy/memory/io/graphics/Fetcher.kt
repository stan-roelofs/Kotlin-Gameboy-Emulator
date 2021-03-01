package nl.stanroelofs.gameboy.memory.io.graphics

import Logging
import nl.stanroelofs.gameboy.memory.Oam
import nl.stanroelofs.gameboy.memory.Register
import nl.stanroelofs.gameboy.utils.getBit

// TODO: ugly code, fix this class
abstract class Fetcher(protected val lcdc: Lcdc, protected val wx: Register, protected val wy: Register, protected val scy: Register,
                       protected val scx: Register, protected val ly: Register, protected val oam: Oam, protected val vram: Vram) {

    protected enum class TileState {
        READ_TILE_NUMBER,
        READ_DATA_0,
        READ_DATA_1,
        PUSH,
        SLEEP,
    }

    protected enum class SpriteState {
        READ_SPRITE_TILE_NUMBER,
        READ_SPRITE_DATA_0,
        READ_SPRITE_DATA_1,
        SLEEP
    }

    protected val tileStateMachine = arrayOf(
            TileState.SLEEP,
            TileState.READ_TILE_NUMBER,
            TileState.SLEEP,
            TileState.READ_DATA_0,
            TileState.SLEEP,
            TileState.READ_DATA_1,
            TileState.PUSH,
            TileState.PUSH
    )

    protected val spriteStateMachine = arrayOf(
            SpriteState.SLEEP,
            SpriteState.READ_SPRITE_TILE_NUMBER,
            SpriteState.SLEEP,
            SpriteState.READ_SPRITE_DATA_0,
            SpriteState.SLEEP,
            SpriteState.READ_SPRITE_DATA_1
    )

    protected val logger = Logging.getLogger(Fetcher::class.java.name)

    val bgFifo = Fifo<Pixel>(16)
    val oamFifo = Fifo<Pixel>(16)

    protected var x = 0
    protected var y = 0
    protected var tile = 0
    protected var tileAttributes = 0
    protected var state = 0
        protected set
        get() {
            field %= tileStateMachine.size
            return field
        }

    protected var spriteState = 0
        get() {
            field %= spriteStateMachine.size
            return field
        }

    protected var dropPixels = false
    protected var window = false
    protected var tileData = 0
    protected var tileData2 = 0
    protected var tileLine = 0

    protected var flags = 0
    protected var sprite = SpritePosition(0, 0, 0)
    protected var spriteSize = 0
    protected var spriteRequested = false
    protected var spriteData = 0
    protected var spriteData2 = 0
    protected var spriteTile = 0
    protected var spriteLine = 0
    protected var spriteYFlip = false
    protected var spriteX = 0

    fun reset() {
        spriteState = 0
        state = 0
        x = 0
        y = 0
        tile = 0
        window = false
        tileData = 0
        spriteRequested = false
        spriteData = 0
        spriteData2 = 0
        spriteTile = 0
        tileAttributes = 0
        tileLine = 0
        dropPixels = true
        bgFifo.clear()
        oamFifo.clear()
        spriteX = 0
    }
    // State machine
    abstract fun readTileNumber()
    abstract fun readTileData0()
    abstract fun readTileData1()
    abstract fun readSpriteTileNumber()
    abstract fun readSpriteTileData0()
    abstract fun readSpriteTileData1()

    abstract fun push() : Boolean
    abstract fun pushSprite()

    fun tick() {
        if (!spriteRequested || state < 5 || bgFifo.size < 8) {
            when (tileStateMachine[state]) {
                TileState.READ_TILE_NUMBER -> {
                    readTileNumber()
                }
                TileState.READ_DATA_0 -> {
                    readTileData0()
                }
                TileState.READ_DATA_1 -> {
                    readTileData1()
                }
                TileState.PUSH -> {
                    if (push()) {
                        state = 0
                        x = (x + 1) and 0x1F
                    }
                }
                TileState.SLEEP -> {
                    ++state
                }
            }
        } else {
            when (spriteStateMachine[spriteState]) {
                SpriteState.READ_SPRITE_TILE_NUMBER -> {
                    readSpriteTileNumber()
                }
                SpriteState.READ_SPRITE_DATA_0 -> {
                    readSpriteTileData0()
                }
                SpriteState.READ_SPRITE_DATA_1 -> {
                    readSpriteTileData1()
                }
                SpriteState.SLEEP -> {
                    ++spriteState
                }
            }
        }
    }

    fun startFetchingBackground() {
        dropPixels = true
        x = (scx.value / 8) and 0x1F
        y = (ly.value + scy.value) and 0xFF
    }

    fun startFetchingWindow() {
        window = true
        x = 0
        y = ly.value - wy.value
        state = 0
        bgFifo.clear()
    }

    fun startFetchingSprite(sprite: SpritePosition, offset: Int) {
        this.sprite = sprite
        spriteX = offset
        spriteState = 0
        spriteRequested = true
    }

    fun fetchingSprite() : Boolean {
        return spriteRequested
    }
}

class FetcherCGB(lcdc: Lcdc, wx: Register, wy: Register, scy: Register,
                 scx: Register, ly: Register, oam: Oam, vram: Vram) : Fetcher(lcdc, wx, wy, scy, scx, ly, oam, vram) {

    init {
        reset()
    }

    override fun readTileNumber() {
        val tileMap = if ((!window && lcdc.getBackgroundTileMap()) || (window && lcdc.getWindowTileMap())) 0x9C00 else 0x9800
        val offset = tileMap + (y / 8 * 32) + x
        tileAttributes = vram.readByte(1, offset)
        tile = vram.readByte(0, offset)
        ++state
    }

    override fun readTileData0() {
        val tileBaseAddress = if (lcdc.getTileDataSelect()) 0x8000 else 0x9000
        val tileOffset = if (lcdc.getTileDataSelect()) tile else tile.toByte().toInt()

        val yFlip = tileAttributes.getBit(6)
        var line = y % 8
        if (yFlip)
            line = 7 - line

        tileData = vram.readByte(if (tileAttributes.getBit(3)) 1 else 0,tileBaseAddress + tileOffset * 16 + line * 2)
        ++state
    }

    override fun readTileData1() {
        val tileBaseAddress = if (lcdc.getTileDataSelect()) 0x8000 else 0x9000
        val tileOffset = if (lcdc.getTileDataSelect()) tile else tile.toByte().toInt()

        val yFlip = tileAttributes.getBit(6)
        var line = y % 8
        if (yFlip)
            line = 7 - line

        tileData2 = vram.readByte(if (tileAttributes.getBit(3)) 1 else 0,tileBaseAddress + tileOffset * 16 + line * 2 + 1)
        ++state
        if (dropPixels) {
            state = 0
            dropPixels = false
            return
        }
        if (push()) {
            state = 0
            x = (x + 1) and 0x1F
        }
    }

    override fun readSpriteTileNumber() {
        spriteTile = oam.readByte(sprite.address + 2)
        flags = oam.readByte(sprite.address + 3)
        ++spriteState
    }

    override fun readSpriteTileData0() {
        spriteSize = if (lcdc.getObjectSize()) {
            spriteTile = spriteTile and 0xFE
            16
        } else {
            8
        }

        spriteYFlip = flags.getBit(6)
        spriteLine = if (spriteYFlip) {
            spriteSize - 1 - (ly.value + 16 - sprite.y)
        } else {
            ly.value + 16 - sprite.y
        }

        val bank = if (flags.getBit(3)) 1 else 0
        spriteData = vram.readByte(bank,0x8000 + spriteTile * 16 + spriteLine * 2)
        ++spriteState
    }

    override fun readSpriteTileData1() {
        val bank = if (flags.getBit(3)) 1 else 0
        spriteData2 = vram.readByte(bank,0x8000 + spriteTile * 16 + spriteLine * 2 + 1)
        pushSprite()
        spriteRequested = false
    }

    override fun push() : Boolean {
        val xFlip = tileAttributes.getBit(5)

        if (bgFifo.size <= 8) {
            for (i in 0 until 8) {
                val lowerBit = if (xFlip) (tileData and (1 shl i)) shr i else (tileData and (1 shl (7 - i))) shr (7 - i)
                val upperBit = if (xFlip) (tileData2 and (1 shl i)) shr i else (tileData2 and (1 shl (7 - i))) shr (7 - i)
                val color = lowerBit or (upperBit shl 1)
                bgFifo.push(PixelCGB(color, tileAttributes and 0b111, tileAttributes.getBit(7)))
            }
            return true
        }
        return false
    }

    override fun pushSprite() {
        val xFlip = flags.getBit(5)

        // Push transparent pixels if the oam fifo does not have at least 8 pixels
        if (oamFifo.size < 8) {
            for (i in 0 until 8 - oamFifo.size)
                oamFifo.push(PixelCGB(0, 0, false))
        }

        // Overlay the pixels onto the ones already in the oam fifo
        var j = 0
        for (i in spriteX until 8) {
            val lowerBit = if (xFlip) (spriteData and (1 shl i)) shr i else (spriteData and (1 shl (7 - i))) shr (7 - i)
            val upperBit = if (xFlip) (spriteData2 and (1 shl i)) shr i else (spriteData2 and (1 shl (7 - i))) shr (7 - i)
            val color = lowerBit or (upperBit shl 1)

            val pixel = oamFifo.peek(j) as PixelCGB
            if (pixel.color == 0) {
                pixel.color = color
                pixel.palette = flags and 0b111
                pixel.priority = flags.getBit(7)
            }
            ++j
        }
    }
}

class FetcherDMG (lcdc: Lcdc, wx: Register, wy: Register, scy: Register, scx: Register, ly: Register, oam: Oam, vram: Vram)
    : Fetcher(lcdc, wx, wy, scy, scx, ly, oam, vram) {

    init {
        reset()
    }

    override fun readTileNumber() {
        val tileMap = if ((!window && lcdc.getBackgroundTileMap()) || (window && lcdc.getWindowTileMap())) 0x9C00 else 0x9800
        val offset = tileMap + (y / 8 * 32) + x
        tile = vram.readByte(0, offset)
        ++state
    }

    override fun readTileData0() {
        val tileBaseAddress = if (lcdc.getTileDataSelect()) 0x8000 else 0x9000
        val tileOffset = if (lcdc.getTileDataSelect()) tile else tile.toByte().toInt()
        tileData = vram.readByte(0,tileBaseAddress + tileOffset * 16 + (y % 8) * 2)
        ++state
    }

    override fun readTileData1() {
        val tileBaseAddress = if (lcdc.getTileDataSelect()) 0x8000 else 0x9000
        val tileOffset = if (lcdc.getTileDataSelect()) tile else tile.toByte().toInt()
        tileData2 = vram.readByte(0,tileBaseAddress + tileOffset * 16 + (y % 8) * 2 + 1)
        ++state
        if (dropPixels) {
            state = 0
            dropPixels = false
            return
        }
        if (push()) {
            state = 0
            x = (x + 1) and 0x1F
        }
    }

    override fun readSpriteTileNumber() {
        spriteTile = oam.readByte(sprite.address + 2)
        flags = oam.readByte(sprite.address + 3)
        ++spriteState
    }

    override fun readSpriteTileData0() {
        spriteSize = if (lcdc.getObjectSize()) {
            spriteTile = spriteTile and 0xFE
            16
        } else {
            8
        }

        spriteYFlip = flags.getBit(6)
        spriteLine = if (spriteYFlip) {
            spriteSize - 1 - (ly.value + 16 - sprite.y)
        } else {
            ly.value + 16 - sprite.y
        }

        spriteData = vram.readByte(0,0x8000 + spriteTile * 16 + spriteLine * 2)
        ++spriteState
    }

    override fun readSpriteTileData1() {
        spriteData2 = vram.readByte(0,0x8000 + spriteTile * 16 + spriteLine * 2 + 1)
        pushSprite()
        spriteRequested = false
    }

    override fun push() : Boolean {
        if (bgFifo.size <= 8) {
            for (i in 0 until 8) {
                val lowerBit = (tileData and (1 shl (7 - i))) shr (7 - i)
                val upperBit = (tileData2 and (1 shl (7 - i))) shr (7 - i)
                val color = lowerBit or (upperBit shl 1)
                bgFifo.push(PixelDMG(color, palette1 = false, priority = false))
            }
            return true
        }
        return false
    }

    override fun pushSprite() {
        val xFlip = flags.getBit(5)

        // Push transparent pixels if the oam fifo does not have at least 8 pixels
        if (oamFifo.size < 8) {
            for (i in 0 until 8 - oamFifo.size)
                oamFifo.push(PixelDMG(0, palette1 = false, priority = false))
        }

        // Overlay the pixels onto the ones already in the oam fifo
        for (i in spriteX until 8) {
            val lowerBit = if (xFlip) (spriteData and (1 shl i)) shr i else (spriteData and (1 shl (7 - i))) shr (7 - i)
            val upperBit = if (xFlip) (spriteData2 and (1 shl i)) shr i else (spriteData2 and (1 shl (7 - i))) shr (7 - i)
            val color = lowerBit or (upperBit shl 1)

            val pixel = oamFifo.peek(i) as PixelDMG
            if (pixel.color == 0) {
                pixel.color = color
                pixel.palette1 = flags.getBit(4)
                pixel.priority = flags.getBit(7)
            }
        }
    }
}