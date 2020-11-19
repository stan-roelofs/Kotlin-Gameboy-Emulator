package gameboy.memory.io.graphics

import gameboy.memory.Mmu
import gameboy.utils.getBit

class FetcherDMG (bgFifo : Fifo<Pixel>, oamFifo: Fifo<Pixel>, mmu: Mmu) : Fetcher(bgFifo, oamFifo, mmu) {

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
    }

    fun tick() {
        if (!spriteRequested || state < 5 || bgFifo.size < 8) {
            when (tileStateMachine[state]) {
                TileState.READ_TILE_NUMBER -> {
                    val lcdc = mmu.readByte(Mmu.LCDC)
                    val tileMap = if ((!window && lcdc.getBit(3)) || (window && lcdc.getBit(6))) 0x9C00 else 0x9800
                    tile = mmu.readByte(tileMap + (y / 8 * 32) + x)
                    ++state
                }
                TileState.READ_DATA_0 -> {
                    val lcdc = mmu.readByte(Mmu.LCDC)
                    val tileBaseAddress = if (lcdc.getBit(4)) 0x8000 else 0x9000
                    val tileOffset = if (lcdc.getBit(4)) tile else tile.toByte().toInt()
                    tileData = mmu.readByte(tileBaseAddress + tileOffset * 16 + (y % 8) * 2)
                    ++state
                }
                TileState.READ_DATA_1 -> {
                    val lcdc = mmu.readByte(Mmu.LCDC)
                    val tileBaseAddress = if (lcdc.getBit(4)) 0x8000 else 0x9000
                    val tileOffset = if (lcdc.getBit(4)) tile else tile.toByte().toInt()
                    tileData2 = mmu.readByte(tileBaseAddress + tileOffset * 16 + (y % 8) * 2 + 1)
                    ++state
                    if (dropPixels) {
                        state = 0
                        dropPixels = false
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
                    spriteTile = mmu.readByte(sprite.address + 2)
                    flags = mmu.readByte(sprite.address + 3)
                    ++spriteState
                }
                SpriteState.READ_SPRITE_DATA_0 -> {
                    spriteSize = if (mmu.readByte(Mmu.LCDC).getBit(2)) {
                        spriteTile = spriteTile and 0xFE
                        16
                    } else {
                        8
                    }

                    spriteYFlip = flags.getBit(6)
                    spriteLine = if (spriteYFlip) {
                        spriteSize - 1 - (mmu.readByte(Mmu.LY) + 16 - sprite.y)
                    } else {
                        mmu.readByte(Mmu.LY) + 16 - sprite.y
                    }

                    spriteData = mmu.readByte(0x8000 + spriteTile * 16 + spriteLine * 2)
                    ++spriteState
                }
                SpriteState.READ_SPRITE_DATA_1 -> {
                    spriteData2 = mmu.readByte(0x8000 + spriteTile * 16 + spriteLine * 2 + 1)
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

    fun startFetchingBackground() {
        dropPixels = true
        x = (mmu.readByte(Mmu.SCX) / 8) and 0x1F
        y = (mmu.readByte(Mmu.LY) + mmu.readByte(Mmu.SCY)) and 0xFF
    }

    fun startFetchingWindow() {
        window = true
        x = (mmu.readByte(Mmu.WX) / 8) and 0x1F
        y = mmu.readByte(Mmu.LY) - mmu.readByte(Mmu.WY)
        state = 0
        bgFifo.clear()
    }

    fun startFetchingSprite(sprite: SpritePosition) {
        this.sprite = sprite
        spriteState = 0
        spriteRequested = true
    }

    fun fetchingSprite() : Boolean {
        return spriteRequested
    }
}