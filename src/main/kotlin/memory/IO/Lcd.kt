package memory.IO

import memory.Memory
import memory.Mmu
import utils.Log
import utils.getBit
import utils.setBit
import utils.toHexString

class Lcd : Memory {

    var screen = Array(256) {IntArray(256)}

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

    var cycleCounter = 0

    override fun reset() {
        LCDC = 0x91
        LY = 0x00
        LYC = 0x00
        STAT = 0x00
        SCY = 0x00
        SCX = 0x00
        WY = 0x00
        WX = 0x00
        BGP = 0xFC
        OBP0 = 0xFF
        OBP1 = 0xFF
        cycleCounter = 0
    }

    fun tick(cyclesElapsed: Int) {
        if (!LcdEnabled()) {
            return
        }

        cycleCounter += cyclesElapsed

        val mode = STAT and 0b11
        when (mode) {
            Mode.HBLANK.mode -> {
                if (cycleCounter >= Mode.HBLANK.cycles) {
                    cycleCounter = 0

                    LY++

                    if (LY >= 144) {
                        setMode(Mode.VBLANK)

                        if (STAT.getBit(4)) {
                            requestInterrupt(1)
                        }

                    } else {
                        setMode(Mode.OAM_SEARCH)

                        if (STAT.getBit(5)) {
                            requestInterrupt(1)
                        }
                    }
                }
            }
            Mode.VBLANK.mode -> {
                if (cycleCounter >= Mode.VBLANK.cycles) {
                    cycleCounter = 0

                    if (LY == 144) {
                        requestInterrupt(0)
                    }

                    LY++
                    if (LY == 154) {
                        LY = 0
                        setMode(Mode.OAM_SEARCH)

                        if (STAT.getBit(5)) {
                            requestInterrupt(1)
                        }
                    }
                }
            }
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

                    if (STAT.getBit(3)) {
                        requestInterrupt(1)
                    }

                    renderScanline()
                }
            }
        }

        STAT = setBit(STAT, 2, LY == LYC)

        if (STAT.getBit(6) && STAT.getBit(2)) {
            requestInterrupt(1)
        }
    }

    private fun setMode(mode: Mode) {
        val nr = mode.mode
        STAT = setBit(STAT, 0, nr.getBit(0))
        STAT = setBit(STAT, 1, nr.getBit(1))
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.LCDC -> this.LCDC
            Mmu.LY -> {
                if (LcdEnabled()) {
                    this.LY
                } else {
                    0
                }
            }
            Mmu.LYC -> this.LYC
            Mmu.STAT -> {
                if (LcdEnabled()) {
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
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Lcd")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.LCDC -> {
                this.LCDC = newVal
                if (!LcdEnabled()) {
                    cycleCounter = 0
                }
            }
            Mmu.LY -> this.LY = 0
            Mmu.LYC -> this.LYC = newVal
            Mmu.STAT -> this.STAT = (newVal and 0b11111000) // Last three bits are read-only
            Mmu.SCY -> this.SCY = newVal
            Mmu.SCX -> this.SCX = newVal
            Mmu.WY -> this.WY = newVal
            Mmu.WX -> this.WX = newVal
            Mmu.BGP -> this.BGP = newVal
            Mmu.OBP0 -> this.OBP0 = newVal
            Mmu.OBP1 -> this.OBP1 = newVal
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Lcd")
        }
    }

    private fun renderScanline() {
        if (LcdEnabled()) {
            renderBackground()
            renderWindow()
            renderSprites()
        } else {
            for (i in 0..255) {
                for (j in 0..255) {
                    screen[i][j] = 0
                }
            }
        }
    }

    private fun renderWindow() {
        val windowEnabled = LCDC.getBit(5)
        if (windowEnabled) {
            val mmu = Mmu.instance
            // Read location of tile map from LCDC
            val windowMap = if (LCDC.getBit(6)) 0x9C00 else 0x9800

            // Read location of tiles from LCDC
            val tilesAddress = if (LCDC.getBit(4)) 0x8000 else 0x8800

            // Calculate address start which contains the tiles of the current line
            val currentLine = LY
            val lineAddress = windowMap + (currentLine / 8) * 32

            // One line has 32 tiles
            for (i in lineAddress until lineAddress + 32) {
                val tileId = if (tilesAddress == 0x8800) {
                    mmu.readByte(i).toByte().toInt() + 128
                } else {
                    mmu.readByte(i)
                }

                // Calculate the starting address of the tile, each tile is stored as 16 bytes
                val tileStart = tilesAddress + (tileId * 16)

                // Calculate the offset from the start of the tile to the current line, each line is stored as 2 bytes
                val lineOffset = (currentLine % 8) * 2

                // Read the 2 bytes that represent the current line in the tile
                val byte = mmu.readByte(tileStart + lineOffset)
                val byte2 = mmu.readByte(tileStart + lineOffset + 1)

                for (x in 0..7) {
                    // Read color indicator
                    val LSB = if (byte.getBit(x)) 1 else 0
                    val MSB = if (byte2.getBit(x)) 2 else 0
                    val color = LSB + MSB

                    // Read background palette
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
                    screen[WX - 7 + (i - lineAddress) * 8 + (7 - x)][currentLine + WY] = newColor
                }
            }
        }
    }

    private fun renderBackground() {
        val bgEnabled = LCDC.getBit(0)
        if (bgEnabled) {
            val mmu = Mmu.instance
            // Read location of tile map from LCDC
            val backgroundMap = if (LCDC.getBit(3)) 0x9C00 else 0x9800

            // Read location of tiles from LCDC
            val tilesAddress = if (LCDC.getBit(4)) 0x8000 else 0x8800

            // Calculate address start which contains the tiles of the current line
            val currentLine = LY
            val lineAddress = backgroundMap + (currentLine / 8) * 32

            // One line has 32 tiles
            for (i in lineAddress until lineAddress + 32) {
                val tileId = if (tilesAddress == 0x8800) {
                    mmu.readByte(i).toByte().toInt() + 128
                } else {
                    mmu.readByte(i)
                }

                // Calculate the starting address of the tile, each tile is stored as 16 bytes
                val tileStart = tilesAddress + (tileId * 16)

                // Calculate the offset from the start of the tile to the current line, each line is stored as 2 bytes
                val lineOffset = (currentLine % 8) * 2

                // Read the 2 bytes that represent the current line in the tile
                val byte = mmu.readByte(tileStart + lineOffset)
                val byte2 = mmu.readByte(tileStart + lineOffset + 1)

                for (x in 0..7) {
                    // Read color indicator
                    val LSB = if (byte.getBit(x)) 1 else 0
                    val MSB = if (byte2.getBit(x)) 2 else 0
                    val color = LSB + MSB

                    // Read background palette
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

                    try {
                        screen[(i - lineAddress) * 8 + (7 - x)][currentLine] = newColor
                    } catch (e: Exception) {
                        Log.e("$i $lineAddress $currentLine $backgroundMap")
                        Log.e("${(i - lineAddress) * 8 + (7 - x)}")
                        Log.e("$i $x")
                        throw e
                    }
                }
            }
        }
    }

    private fun renderSprites() {
        val mmu = Mmu.instance

        val spritesDisplay = LCDC.getBit(1)

        if (spritesDisplay) {
            // For each sprite in OAM (each sprite consists of 4 bytes)
            for (i in 0xFE00 until 0xFEA0 step 4) {
                val spriteY = mmu.readByte(i) - 16

                // If sprite overlaps with current line
                if (LY in spriteY..spriteY + 7) {
                    val spriteX = mmu.readByte(i + 1) - 8
                    val spriteId = mmu.readByte(i + 2)
                    val spriteFlags = mmu.readByte(i + 3)

                    val priority = spriteFlags.getBit(7)
                    val yFlip = spriteFlags.getBit(6)
                    val xFlip = spriteFlags.getBit(5)
                    val palette = spriteFlags.getBit(4)

                    val lineOffset = (7 - (spriteY + 7 - LY)) * 2
                    // Offset of current line
                    if (yFlip) {
                        //lineOffset = (8 - (LY % 8)) * 2
                    }

                    // Read the two bytes that together describe the current line
                    val byte = mmu.readByte(0x8000 + lineOffset + spriteId * 16) // Each tile occupies 16 bytes
                    val byte2 = mmu.readByte(0x8000 + lineOffset + spriteId * 16 + 1)

                    for (x in 0..7) {
                        var actualX = x
                        if (xFlip) {
                            actualX = 7 - x
                        }
                        val LSB = if (byte.getBit(actualX)) 1 else 0
                        val MSB = if (byte2.getBit(actualX)) 2 else 0
                        val color = LSB + MSB

                        val objPalette = if (palette) OBP1 else OBP0

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

                        if ((!priority || screen[spriteX + (7 - actualX)][LY] == 0) && color != 0) {
                            screen[spriteX + (7 - actualX)][LY] = newColor
                        }
                    }
                }
            }
        }
    }

    private fun LcdEnabled(): Boolean {
        return LCDC.getBit(7)
    }

    enum class Mode(val mode: Int, val cycles: Int) {
        HBLANK(0, 204),
        VBLANK(1, 456),
        OAM_SEARCH(2, 80),
        LCD_TRANSFER(3, 172),
    }
}