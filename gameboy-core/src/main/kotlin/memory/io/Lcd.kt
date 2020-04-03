package memory.io

import memory.Memory
import memory.Mmu
import utils.getBit
import utils.setBit
import utils.toHexString
import java.util.*

class Lcd : Memory, Observable() {

    // Internal screen buffer
    val screenBuffer = ByteArray(144 * 160)
    val backgroundBuffer = Array(144) {IntArray(160)}
    val windowBuffer = Array(144) {IntArray(160)}
    val spritesBuffer = Array(144) {IntArray(160)}

    // Video RAM memory
    private val vram = IntArray(0x2000)

    // List of sprites
    private val sprites = Array(40) {Sprite()}

    // List of tiles in the video ram
    private val tiles = Array(384) {Array(16) {IntArray(8)}}

    private var LCDC = 0
    private var LY = 0
    private var LYC = 0
    private var STAT = 0
    private var SCY = 0
    private var SCX = 0
    private var WY = 0
    private var WX = 0
    private var BGP = 0
    private var OBP0 = 0
    private var OBP1 = 0

    private var isAnyStat = false
    var cycleCounter = 0

    init {
        reset()
    }

    override fun reset() {
        LCDC = 0x91
        LY = 0x00
        LYC = 0x00
        STAT = 0b10 // Start in mode 2
        SCY = 0x00
        SCX = 0x00
        WY = 0x00
        WX = 0x00
        BGP = 0xFC
        OBP0 = 0xFF
        OBP1 = 0xFF

        vram.fill(0)
        screenBuffer.fill(0)

        for (i in 0 until 144) {
            backgroundBuffer[i].fill(0)
            windowBuffer[i].fill(0)
            spritesBuffer[i].fill(0)
        }

        for (i in 0 until sprites.size) {
            sprites[i] = Sprite()
        }

        for (i in 0 until tiles.size) {
            for (j in 0 until tiles[i].size) {
                for (k in 0 until tiles[i][j].size) {
                    tiles[i][j][k] = 0
                }
            }
        }

        cycleCounter = 0
    }

    fun tick(cyclesElapsed: Int) {
        if (!lcdEnabled()) {
            return
        }

        cycleCounter += cyclesElapsed

        val mode = getMode()
        when (mode) {
            Mode.OAM_SEARCH.mode -> {
                if (cycleCounter >= Mode.OAM_SEARCH.cycles) {
                    cycleCounter = 0
                    setMode(Mode.LCD_TRANSFER)
                }
            }
            Mode.LCD_TRANSFER.mode -> {
                if (cycleCounter >= Mode.LCD_TRANSFER.cycles) {
                    cycleCounter = 0
                    setMode(Mode.HBLANK)

                    renderScanline()
                }
            }
            Mode.HBLANK.mode -> {
                if (cycleCounter >= Mode.HBLANK.cycles) {
                    cycleCounter = 0
                    LY++

                    if (LY == 144) {
                        setMode(Mode.VBLANK)

                        requestInterrupt(0)

                        setChanged()
                        notifyObservers(screenBuffer)

                    } else {
                        setMode(Mode.OAM_SEARCH)
                    }
                }
            }
            Mode.VBLANK.mode -> {
                if (cycleCounter >= Mode.VBLANK.cycles) {
                    cycleCounter = 0
                    LY++

                    if (LY > 153) {
                        LY = 0
                        setMode(Mode.OAM_SEARCH)
                    }
                }
            }

        }

        STAT = setBit(STAT, 2, LY == LYC)

        checkStatInterrupts()
    }

    private fun checkStatInterrupts() {
        val mode = getMode()

        if (    (mode == Mode.HBLANK.mode && STAT.getBit(3)) ||
                (mode == Mode.OAM_SEARCH.mode && STAT.getBit(5)) ||
                (mode == Mode.VBLANK.mode && (STAT.getBit(4) || STAT.getBit(5))) ||
                (STAT.getBit(6) && STAT.getBit(2))
        ) {
            if (!isAnyStat) {
                isAnyStat = true
                requestInterrupt(1)
            }
        } else {
            isAnyStat = false
        }
    }

    private fun setMode(mode: Mode) {
        val nr = mode.mode
        STAT = setBit(STAT, 0, nr.getBit(0))
        STAT = setBit(STAT, 1, nr.getBit(1))
    }

    private fun getMode(): Int {
        return STAT and 0b11
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.LCDC -> this.LCDC
            Mmu.LY -> {
                if (lcdEnabled()) {
                    this.LY
                } else {
                    0 // If LCD is off this register is fixed at 0
                }
            }
            Mmu.LYC -> this.LYC
            Mmu.STAT -> {
                if (lcdEnabled()) {
                    this.STAT or 0b10000000 // Bit 7 is always 1
                } else {
                    (this.STAT or 0b10000000) and 0b11111000 // Bits 0-2 return 0 when LCD is off
                }
            }
            Mmu.SCY -> this.SCY
            Mmu.SCX -> this.SCX
            Mmu.WY -> this.WY
            Mmu.WX -> this.WX
            Mmu.BGP -> this.BGP
            Mmu.OBP0 -> this.OBP0
            Mmu.OBP1 -> this.OBP1
            in 0x8000 until 0xA000 -> vram[address - 0x8000]
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Lcd")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.LCDC -> {
                val lcdBefore = lcdEnabled()

                this.LCDC = newVal

                if (lcdBefore && !lcdEnabled()) {
                    cycleCounter = 0
                    setMode(Mode.HBLANK)
                    this.LY = 0
                }
            }
            Mmu.LY -> this.LY = 0
            Mmu.LYC -> this.LYC = newVal
            Mmu.STAT -> this.STAT = this.STAT or (newVal and 0b11111000) // Last three bits are read-only
            Mmu.SCY -> this.SCY = newVal
            Mmu.SCX -> this.SCX = newVal
            Mmu.WY -> this.WY = newVal
            Mmu.WX -> this.WX = newVal
            Mmu.BGP -> this.BGP = newVal
            Mmu.OBP0 -> this.OBP0 = newVal
            Mmu.OBP1 -> this.OBP1 = newVal

            in 0x8000 until 0xA000 -> {
                vram[address - 0x8000] = value and 0xFF
                if (address <= 0x97FF) {
                    updateTile(address - 0x8000)
                }
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Lcd")
        }
    }

    private fun updateTile(address: Int) {
        // get base address for this tile row
        val addr = address and 0x1FFE

        // work out which tile and row was updated
        val tile = addr shr 4 and 511
        val y = addr shr 1 and 7

        var sx: Int
        for (i in 0..7) {

            // find bit index for this pixel
            sx = 1 shl 7 - i

            val lol = (if (vram[addr] and sx != 0) 1 else 0) or if (vram[addr + 1] and sx != 0) 2 else 0

            tiles[tile][y][i] = lol
        }
    }

    private fun renderScanline() {
        val row = IntArray(160)
        renderBackground(row)
        renderWindow(row)
        renderSprites(row)
    }

    private fun renderWindow(row: IntArray) {
        val windowEnabled = LCDC.getBit(5)

        if (windowEnabled && LY >= WY) {
            val windowMap = LCDC.getBit(6)
            val windowTiles = !LCDC.getBit(4)

            // Read location of tile map from LCDC
            var mapOffset = if (windowMap) 0x1C00 else 0x1800

            // Add the offset to the current line
            mapOffset += (((LY - WY) and 0xFF) shr 3) shl 5

            var newWX = WX - 7
            if (newWX < 0) {
                newWX += 255
            }
            // Add the offset in the horizontal direction
            var lineOffset = newWX shr 3

            val y = (LY - WY) and 7
            var x = newWX and 7

            var color: Int
            var tile = vram[mapOffset + lineOffset]

            if (windowTiles && (tile < 128)) {
                tile += 256
            }

            for (i in 0 until 160) {
                color = tiles[tile][y][x]

                val bgPalette = BGP

                // Get color corresponding to color indicator using palette
                var newColor = 0
                when (color) {
                    0b00 -> {
                        newColor = setBit(newColor, 0, bgPalette.getBit(0))
                        newColor = setBit(newColor, 1, bgPalette.getBit(1))
                    }
                    0b01 -> {
                        newColor = setBit(newColor, 0, bgPalette.getBit(2))
                        newColor = setBit(newColor, 1, bgPalette.getBit(3))
                    }
                    0b10 -> {
                        newColor = setBit(newColor, 0, bgPalette.getBit(4))
                        newColor = setBit(newColor, 1, bgPalette.getBit(5))
                    }
                    0b11 -> {
                        newColor = setBit(newColor, 0, bgPalette.getBit(6))
                        newColor = setBit(newColor, 1, bgPalette.getBit(7))
                    }
                }

                windowBuffer[LY][i] = newColor
                screenBuffer[LY * 160 + i] = newColor.toByte()
                row[i] = color

                x++
                if (x == 8) {
                    x = 0
                    lineOffset = (lineOffset + 1) and 31
                    tile = vram[mapOffset + lineOffset]
                    if (windowTiles && tile < 128) {
                        tile += 256
                    }
                }
            }
        }
    }

    private fun renderBackground(row: IntArray) {
        val bgEnabled = LCDC.getBit(0)
        if (bgEnabled) {
            val backgroundMap = LCDC.getBit(3)
            val backgroundTiles = !LCDC.getBit(4)

            // Read location of tile map from LCDC
            var mapOffset = if (backgroundMap) 0x1C00 else 0x1800

            // Add the offset to the current line
            mapOffset += (((LY + SCY) and 0xFF) shr 3) shl 5

            // Add the offset in the horizontal direction
            var lineOffset = SCX shr 3

            val y = LY + SCY and 7
            var x = SCX and 7

            var color: Int
            var tile = vram[mapOffset + lineOffset]

            if (backgroundTiles && (tile < 128)) {
                tile += 256
            }

            for (i in 0 until 160) {
                color = tiles[tile][y][x]

                val bgPalette = BGP

                // Get color corresponding to color indicator using palette
                var newColor = 0
                when (color) {
                    0b00 -> {
                        newColor = setBit(newColor, 0, bgPalette.getBit(0))
                        newColor = setBit(newColor, 1, bgPalette.getBit(1))
                    }
                    0b01 -> {
                        newColor = setBit(newColor, 0, bgPalette.getBit(2))
                        newColor = setBit(newColor, 1, bgPalette.getBit(3))
                    }
                    0b10 -> {
                        newColor = setBit(newColor, 0, bgPalette.getBit(4))
                        newColor = setBit(newColor, 1, bgPalette.getBit(5))
                    }
                    0b11 -> {
                        newColor = setBit(newColor, 0, bgPalette.getBit(6))
                        newColor = setBit(newColor, 1, bgPalette.getBit(7))
                    }
                }

                backgroundBuffer[LY][i] = newColor
                screenBuffer[LY * 160 + i] = newColor.toByte()
                row[i] = color

                x++
                if (x == 8) {
                    x = 0
                    lineOffset = (lineOffset + 1) and 31
                    tile = vram[mapOffset + lineOffset]
                    if (backgroundTiles && tile < 128) {
                        tile += 256
                    }
                }
            }
        }
    }

    private fun renderSprites(row: IntArray) {
        val spritesEnabled = LCDC.getBit(1)
        if (spritesEnabled) {
            val spriteSize = if (LCDC.getBit(2)) 16 else 8

            for (i in 0 until 40) {
                val obj = sprites[i]

                // Check if this sprite falls on this scanline
                if (obj.y <= LY && (obj.y + spriteSize) > LY) {

                    val temp = if (spriteSize == 16) {
                        if (LY - obj.y < 8) {
                            obj.tileNumber and 0xFE
                        } else {
                            obj.tileNumber or 0x1
                        }
                    } else {
                        obj.tileNumber
                    }

                    var spriteRow = LY - obj.y
                    if (spriteRow >= 8) {
                        spriteRow -= 8
                    }

                    val tilerow: IntArray = if (obj.isYFlip) {
                        tiles[temp][spriteSize - 1 - spriteRow]
                    } else {
                        tiles[temp][spriteRow]
                    }

                    var color: Int

                    for (x in 0..7) {
                        // If this pixel is still on-screen, AND
                        // if it's not colour 0 (transparent), AND
                        // if this sprite has priority OR shows under the bg
                        // then render the pixel
                        color = tilerow[if (obj.isXFlip) 7 - x else x]

                        if (obj.x + x in 0 until 160 && color != 0 && (!obj.priority || row[obj.x + x] == 0)) {
                            // Palette to use for this sprite
                            val objPalette = if (obj.isPalette1) OBP1 else OBP0

                            var newColor = 0
                            when (color) {
                                0b01 -> {
                                    newColor = setBit(newColor, 0, objPalette.getBit(2))
                                    newColor = setBit(newColor, 1, objPalette.getBit(3))
                                }
                                0b10 -> {
                                    newColor = setBit(newColor, 0, objPalette.getBit(4))
                                    newColor = setBit(newColor, 1, objPalette.getBit(5))
                                }
                                0b11 -> {
                                    newColor = setBit(newColor, 0, objPalette.getBit(6))
                                    newColor = setBit(newColor, 1, objPalette.getBit(7))
                                }
                            }

                            spritesBuffer[LY][obj.x + x] = newColor
                            screenBuffer[LY * 160 + obj.x + x] = newColor.toByte()
                        }
                    }
                }
            }
        }
    }

    fun updateSprite(address: Int, value: Int) {
        val addr = address - 0xFE00

        if (addr < 0) {
            throw IllegalArgumentException("Invalid sprite address: ${address.toHexString()}")
        }

        // Get sprite number index, each sprite is stored as 4 bytes so we divide by 4 (shift right 2 bits) to get the index
        val spriteNumber = addr shr 2
        if (spriteNumber < 40) {
            when (addr and 0b11) {
                // First byte of the sprite is Y-coordinate
                0b00 -> sprites[spriteNumber].y = value - 16

                // Second byte of the sprite is X-coordinate
                0b01 -> sprites[spriteNumber].x = value - 8

                // Third byte of the sprite is the tile number
                0b10 -> sprites[spriteNumber].tileNumber = value

                // Fourth byte of the sprite is the sprite options
                0b11 -> {
                    sprites[spriteNumber].isPalette1 = value.getBit(4)
                    sprites[spriteNumber].isXFlip = value.getBit(5)
                    sprites[spriteNumber].isYFlip = value.getBit(6)
                    sprites[spriteNumber].priority = value.getBit(7)
                }
            }
        }
    }

    private fun lcdEnabled(): Boolean {
        return LCDC.getBit(7)
    }

    enum class Mode(val mode: Int, val cycles: Int) {
        HBLANK(0, 204),
        VBLANK(1, 456),
        OAM_SEARCH(2, 80),
        LCD_TRANSFER(3, 172),
    }

    private class Sprite {
        var y = 0
        var x = 0
        var tileNumber = 0
        var priority = false
        var isYFlip = false
        var isXFlip = false
        var isPalette1 = false
    }
}