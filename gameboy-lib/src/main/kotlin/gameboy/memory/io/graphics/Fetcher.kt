package gameboy.memory.io.graphics

import gameboy.memory.Oam
import gameboy.memory.Register
import gameboy.utils.getBit

abstract class Fetcher(protected val lcdc: Lcdc, protected val wx: Register, protected val wy: Register, protected val scy: Register,
                       protected val scx: Register, protected val ly: Register, protected val oam: Oam, protected val vram: Vram) {

    val bgFifo = Fifo<Pixel>(16)
    val oamFifo = Fifo<Pixel>(16)

    abstract fun reset()
    abstract fun startFetchingBackground()
    abstract fun startFetchingWindow()
    abstract fun startFetchingSprite(sprite: SpritePosition)
    abstract fun fetchingSprite(): Boolean
    abstract fun tick()
}

class FetcherCGB(lcdc: Lcdc, wx: Register, wy: Register, scy: Register,
                 scx: Register, ly: Register, oam: Oam, vram: Vram) : Fetcher(lcdc, wx, wy, scy, scx, ly, oam, vram) {

    enum class TileState {
        READ_TILE_NUMBER,
        READ_DATA_0,
        READ_DATA_1,
        PUSH,
        SLEEP,
    }

    enum class SpriteState {
        READ_SPRITE_TILE_NUMBER,
        READ_SPRITE_DATA_0,
        READ_SPRITE_DATA_1,
        SLEEP
    }

    private val tileStateMachine = arrayOf(
            TileState.SLEEP,
            TileState.READ_TILE_NUMBER,
            TileState.SLEEP,
            TileState.READ_DATA_0,
            TileState.SLEEP,
            TileState.READ_DATA_1,
            TileState.PUSH,
            TileState.PUSH
    )

    private val spriteStateMachine = arrayOf(
            SpriteState.SLEEP,
            SpriteState.READ_SPRITE_TILE_NUMBER,
            SpriteState.SLEEP,
            SpriteState.READ_SPRITE_DATA_0,
            SpriteState.SLEEP,
            SpriteState.READ_SPRITE_DATA_1
    )

    private var x = 0
    private var y = 0
    private var tile = 0
    private var tileAttributes = 0
    var state = 0
        private set
        get() {
            field %= tileStateMachine.size
            return field
        }

    private var spriteState = 0
        get() {
            field %= spriteStateMachine.size
            return field
        }

    private var dropPixels = false
    private var window = false
    private var tileData = 0
    private var tileData2 = 0
    private var tileLine = 0

    private var flags = 0
    private var sprite = SpritePosition(0, 0, 0)
    private var spriteSize = 0
    private var spriteRequested = false
    private var spriteData = 0
    private var spriteData2 = 0
    private var spriteTile = 0
    private var spriteLine = 0
    private var spriteYFlip = false

    override fun reset() {
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
    }

    override fun tick() {
        if (!spriteRequested || state < 5 || bgFifo.size < 8) {
            when (tileStateMachine[state]) {
                TileState.READ_TILE_NUMBER -> {
                    val tileMap = if ((!window && lcdc.getBackgroundTileMap()) || (window && lcdc.getWindowTileMap())) 0x9C00 else 0x9800
                    tileAttributes = vram.readByte(1, tileMap + (y / 8 * 32) + x)
                    tile = vram.readByte(0, tileMap + (y / 8 * 32) + x)
                    ++state
                }
                TileState.READ_DATA_0 -> {
                    val tileBaseAddress = if (lcdc.getTileDataSelect()) 0x8000 else 0x9000
                    val tileOffset = if (lcdc.getTileDataSelect()) tile else tile.toByte().toInt()
                    tileData = vram.readByte(if (tileAttributes.getBit(3)) 1 else 0,tileBaseAddress + tileOffset * 16 + (y % 8) * 2)
                    ++state
                }
                TileState.READ_DATA_1 -> {
                    val tileBaseAddress = if (lcdc.getTileDataSelect()) 0x8000 else 0x9000
                    val tileOffset = if (lcdc.getTileDataSelect()) tile else tile.toByte().toInt()
                    tileData2 = vram.readByte(if (tileAttributes.getBit(3)) 1 else 0,tileBaseAddress + tileOffset * 16 + (y % 8) * 2 + 1)
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
                    spriteTile = oam.readByte(sprite.address + 2)
                    flags = oam.readByte(sprite.address + 3)
                    ++spriteState
                }
                SpriteState.READ_SPRITE_DATA_0 -> {
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
                SpriteState.READ_SPRITE_DATA_1 -> {
                    spriteData2 = vram.readByte(0,0x8000 + spriteTile * 16 + spriteLine * 2 + 1)
                    pushSprite()
                    spriteRequested = false
                }
                SpriteState.SLEEP -> {
                    ++spriteState
                }
            }
        }
    }

    private fun push() : Boolean {
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

    private fun pushSprite() {
        val xFlip = flags.getBit(5)

        // Push transparent pixels if the oam fifo does not have at least 8 pixels
        if (oamFifo.size < 8) {
            for (i in 0 until 8 - oamFifo.size)
                oamFifo.push(PixelCGB(0, 0, false))
        }

        // Overlay the pixels onto the ones already in the oam fifo
        for (i in 0 until 8) {
            val lowerBit = if (xFlip) (spriteData and (1 shl i)) shr i else (spriteData and (1 shl (7 - i))) shr (7 - i)
            val upperBit = if (xFlip) (spriteData2 and (1 shl i)) shr i else (spriteData2 and (1 shl (7 - i))) shr (7 - i)
            val color = lowerBit or (upperBit shl 1)

            val pixel = oamFifo.peek(i) as PixelCGB
            if (pixel.color == 0) {
                pixel.color = color
                pixel.palette = flags and 0b111
                pixel.priority = flags.getBit(7)
            }
        }
    }

    override fun startFetchingBackground() {
        dropPixels = true
        x = (scx.value / 8) and 0x1F
        y = (ly.value + scy.value) and 0xFF
    }

    override fun startFetchingWindow() {
        window = true
        x = 0
        y = ly.value - wy.value
        state = 0
        bgFifo.clear()
    }

    override fun startFetchingSprite(sprite: SpritePosition) {
        this.sprite = sprite
        spriteState = 0
        spriteRequested = true
    }

    override fun fetchingSprite() : Boolean {
        return spriteRequested
    }
}

class FetcherDMG (lcdc: Lcdc, wx: Register, wy: Register, scy: Register, scx: Register, ly: Register, oam: Oam, vram: Vram)
    : Fetcher(lcdc, wx, wy, scy, scx, ly, oam, vram) {

    enum class TileState {
        READ_TILE_NUMBER,
        READ_DATA_0,
        READ_DATA_1,
        PUSH,
        SLEEP,
    }

    enum class SpriteState {
        READ_SPRITE_TILE_NUMBER,
        READ_SPRITE_DATA_0,
        READ_SPRITE_DATA_1,
        SLEEP
    }

    private val tileStateMachine = arrayOf(
            TileState.SLEEP,
            TileState.READ_TILE_NUMBER,
            TileState.SLEEP,
            TileState.READ_DATA_0,
            TileState.SLEEP,
            TileState.READ_DATA_1,
            TileState.PUSH,
            TileState.PUSH
    )

    private val spriteStateMachine = arrayOf(
            SpriteState.SLEEP,
            SpriteState.READ_SPRITE_TILE_NUMBER,
            SpriteState.SLEEP,
            SpriteState.READ_SPRITE_DATA_0,
            SpriteState.SLEEP,
            SpriteState.READ_SPRITE_DATA_1
    )

    private var x = 0
    private var y = 0
    private var tile = 0
    var state = 0
        private set
        get() {
            field %= tileStateMachine.size
            return field
        }

    private var spriteState = 0
        get() {
            field %= spriteStateMachine.size
            return field
        }

    private var dropPixels = false
    private var window = false
    private var tileData = 0
    private var tileData2 = 0

    private var flags = 0
    private var sprite = SpritePosition(0, 0, 0)
    private var spriteSize = 0
    private var spriteRequested = false
    private var spriteData = 0
    private var spriteData2 = 0
    private var spriteTile = 0
    private var spriteLine = 0
    private var spriteYFlip = false

    override fun reset() {
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
        bgFifo.clear()
        oamFifo.clear()
    }

    override fun tick() {
        if (!spriteRequested || state < 5 || bgFifo.size < 8) {
            when (tileStateMachine[state]) {
                TileState.READ_TILE_NUMBER -> {
                    val tileMap = if ((!window && lcdc.getBackgroundTileMap()) || (window && lcdc.getWindowTileMap())) 0x9C00 else 0x9800
                    tile = vram.readByte(0, tileMap + (y / 8 * 32) + x)
                    ++state
                }
                TileState.READ_DATA_0 -> {
                    val tileBaseAddress = if (lcdc.getTileDataSelect()) 0x8000 else 0x9000
                    val tileOffset = if (lcdc.getTileDataSelect()) tile else tile.toByte().toInt()
                    tileData = vram.readByte(0,tileBaseAddress + tileOffset * 16 + (y % 8) * 2)
                    ++state
                }
                TileState.READ_DATA_1 -> {
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
                    spriteTile = oam.readByte(sprite.address + 2)
                    flags = oam.readByte(sprite.address + 3)
                    ++spriteState
                }
                SpriteState.READ_SPRITE_DATA_0 -> {
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
                SpriteState.READ_SPRITE_DATA_1 -> {
                    spriteData2 = vram.readByte(0,0x8000 + spriteTile * 16 + spriteLine * 2 + 1)
                    pushSprite()
                    spriteRequested = false
                }
                SpriteState.SLEEP -> {
                    ++spriteState
                }
            }
        }
    }

    private fun push() : Boolean {
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

    private fun pushSprite() {
        val xFlip = flags.getBit(5)

        // Push transparent pixels if the oam fifo does not have at least 8 pixels
        if (oamFifo.size < 8) {
            for (i in 0 until 8 - oamFifo.size)
                oamFifo.push(PixelDMG(0, palette1 = false, priority = false))
        }

        // Overlay the pixels onto the ones already in the oam fifo
        for (i in 0 until 8) {
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

    override fun startFetchingBackground() {
        dropPixels = true
        x = (scx.value / 8) and 0x1F
        y = (ly.value + scy.value) and 0xFF
    }

    override fun startFetchingWindow() {
        window = true
        x = 0
        y = ly.value - wy.value
        state = 0
        bgFifo.clear()
    }

    override fun startFetchingSprite(sprite: SpritePosition) {
        this.sprite = sprite
        spriteState = 0
        spriteRequested = true
    }

    override fun fetchingSprite() : Boolean {
        return spriteRequested
    }
}