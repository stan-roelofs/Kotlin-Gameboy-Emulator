package memory

import utils.getBit
import utils.setBit

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
    }

    fun tick(cyclesElapsed: Int) {
        if (!LcdEnabled()) {
            cycleCounter = 0
            LY = 0
            return
        }

        cycleCounter += cyclesElapsed

        val mode = STAT and 0b11
        when (mode) {
            Mode.HBLANK.mode -> {
                if (cycleCounter >= Mode.HBLANK.cycles) {
                    cycleCounter -= Mode.HBLANK.cycles

                    LY++

                    if (LY == 144) {
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
                    cycleCounter -= Mode.VBLANK.cycles

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
                    cycleCounter -= Mode.OAM_SEARCH.cycles
                    setMode(Mode.LCD_TRANSFER)
                }
            }
            Mode.LCD_TRANSFER.mode -> {
                if (cycleCounter >= Mode.LCD_TRANSFER.cycles) {
                    cycleCounter -= Mode.LCD_TRANSFER.cycles
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
                if (LCDC.getBit(7)) {
                    this.LY
                } else {
                    0
                }
            }
            Mmu.LYC -> this.LYC
            Mmu.STAT -> {
                if (LCDC.getBit(7)) {
                    this.STAT or 0b10000000 // Bit 7 is always 1
                } else {
                    this.STAT and 0b11111100
                }
            }
            Mmu.SCY -> this.SCY
            Mmu.SCX -> this.SCX
            Mmu.WY -> this.WY
            Mmu.WX -> this.WX
            Mmu.BGP -> this.BGP
            Mmu.OBP0 -> this.OBP0
            Mmu.OBP1 -> this.OBP1
            else -> throw IllegalArgumentException("Address $address does not belong to Lcd")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.LCDC -> this.LCDC = newVal
            Mmu.LY -> this.LY = 0
            Mmu.LYC -> this.LYC = newVal
            Mmu.STAT -> this.STAT = newVal
            Mmu.SCY -> this.SCY = newVal
            Mmu.SCX -> this.SCX = newVal
            Mmu.WY -> this.WY = newVal
            Mmu.WX -> this.WX = newVal
            Mmu.BGP -> this.BGP = newVal
            Mmu.OBP0 -> this.OBP0 = newVal
            Mmu.OBP1 -> this.OBP1 = newVal
            else -> throw IllegalArgumentException("Address $address does not belong to Lcd")
        }
    }

    private fun renderScanline() {
        if (LcdEnabled()) {
            renderBackground()
            //renderWindow() //TODO
            renderSprites()
        } else {
            for (i in 0..255) {
                for (j in 0..255) {
                    screen[i][j] = 0
                }
            }
        }
    }

    private fun renderBackground() {
        val mmu = Mmu.instance

        // Read location of tile map from LCDC
        val backgroundMap = if (mmu.readByte(Mmu.LCDC).getBit(3)) 0x9C00 else 0x9800

        // Read location of tiles from LCDC
        val tilesAddress = if (mmu.readByte(Mmu.LCDC).getBit(4)) 0x8000 else 0x8800

        // Calculate address start which contains the tiles of the current line
        val currentLine = mmu.readByte(Mmu.LY)
        val lineAddress = backgroundMap + (currentLine / 8) * 32

        // One line has 32 tiles
        for (i in lineAddress until lineAddress + 32) {
            val tileId = if (tilesAddress == 0x8800) {
                mmu.readByte(i).toByte().toInt() + 128
            } else {
                mmu.readByte(i)
            }

            val tileStart = tilesAddress + (tileId * 16)

            val lineOffset = (currentLine % 8) * 2

            val byte = mmu.readByte(tileStart + lineOffset)
            val byte2 = mmu.readByte(tileStart + lineOffset + 1)

            for (x in 0..7) {
                val LSB = if (byte.getBit(x)) 1 else 0
                val MSB = if (byte2.getBit(x)) 2 else 0
                val color = LSB + MSB

                screen[(i - lineAddress) * 8 + (7 - x)][currentLine] = color
            }
        }
    }

    private fun renderSprites() {
        val mmu = Mmu.instance

        // For each sprite in OAM (each sprite consists of 4 bytes)
        for (i in 0xFE00 until 0xFEA0 step 4) {
            val spriteY = mmu.readByte(i) - 16

            // If sprite overlaps with current line
            if (LY in spriteY until spriteY + 8) {
                val spriteX = mmu.readByte(i + 1) - 8
                val spriteId = mmu.readByte(i + 2)
                val spriteFlags = mmu.readByte(i + 3)

                val priority = spriteFlags.getBit(7)
                val yFlip = spriteFlags.getBit(6)
                val xFlip = spriteFlags.getBit(5)
                val palleteNumber = spriteFlags.getBit(4) // TODO

                var lineOffset = (LY % 8) * 2
                // Offset of current line
                if (yFlip) {
                    lineOffset = (8 - (LY % 8)) * 2
                }

                // Read the two bytes that together describe the current line
                val byte = mmu.readByte(0x8000 + lineOffset + spriteId * 16)
                val byte2 = mmu.readByte(0x8000 + lineOffset + spriteId * 16 + 1)

                for (x in 0..7) {
                    var actualX = x
                    if (xFlip) {
                        actualX = 7 - x
                    }
                    val LSB = if (byte.getBit(actualX)) 1 else 0
                    val MSB = if (byte2.getBit(actualX)) 2 else 0
                    val color = LSB + MSB

                    if (!priority || screen[spriteX + (7 - actualX)][LY] == 0) {
                        screen[spriteX + (7 - actualX)][LY] = color
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