package gameboy.memory.io.graphics

import gameboy.memory.Mmu
import gameboy.utils.getBit
import gameboy.utils.toHexString

class LcdCGB(private val mmu: Mmu) : Lcd(mmu) {

    override val screenBuffer = Array(144) { IntArray(160 * 3) }

    private val sprites = Array(40) {SpriteCGB()}

    // Video RAM memory
    val vram = Array(2) {IntArray(0x2000)}

    // List of tiles in the video ram
    private val tiles = Array(384 * 2) {Array(16) {IntArray(8)}}

    private var BCPS = 0
    private var OCPS = 0
    var bgPalettes = IntArray(0x40)
    var objPalettes = IntArray(0x40)

    private var currentBank = 0

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
        BCPS = 0
        OCPS = 0

        bgPalettes.fill(0)
        objPalettes.fill(0)

        for (bank in vram) {
            bank.fill(0)
        }

        for (i in 0 until 144) {
            screenBuffer[i].fill(0)
            backgroundBuffer[i].fill(0)
            windowBuffer[i].fill(0)
            spritesBuffer[i].fill(0)
        }

        for (i in 0 until sprites.size) {
            sprites[i] = SpriteCGB()
        }

        for (i in 0 until tiles.size) {
            for (j in 0 until tiles[i].size) {
                for (k in 0 until tiles[i][j].size) {
                    tiles[i][j][k] = 0
                }
            }
        }

        cycleCounter = 0
        isAnyStat = false
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
                //if (lcdEnabled()) {
                this.STAT or 0b10000000 // Bit 7 is always 1
                //} else {
                //  (this.STAT or 0b10000000) and 0b11111000 // Bits 0-2 return 0 when LCD is off
                //}
            }
            Mmu.SCY -> this.SCY
            Mmu.SCX -> this.SCX
            Mmu.WY -> this.WY
            Mmu.WX -> this.WX
            Mmu.BGP -> this.BGP
            Mmu.OBP0 -> this.OBP0
            Mmu.OBP1 -> this.OBP1
            Mmu.VBK -> this.currentBank or 0b11111110
            Mmu.BCPS -> this.BCPS or 0b01000000
            Mmu.BCPD -> this.bgPalettes[BCPS and 0b00111111]
            Mmu.OCPS -> this.OCPS or 0b01000000
            Mmu.OCPD -> this.objPalettes[OCPS and 0b00111111]
            in 0x8000 until 0xA000 -> vram[currentBank][address - 0x8000]
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
            Mmu.VBK -> this.currentBank = newVal and 0b00000001
            Mmu.BCPS -> this.BCPS = newVal and 0b10111111
            Mmu.BCPD -> {
                this.bgPalettes[BCPS and 0b00111111] = newVal
                if (this.BCPS.getBit(7)) {
                    this.BCPS = (this.BCPS + 1) and 0b10111111
                }
            }
            Mmu.OCPS -> this.OCPS = newVal and 0b10111111
            Mmu.OCPD -> {
                this.objPalettes[OCPS and 0b00111111] = newVal
                if (this.OCPS.getBit(7)) {
                    this.OCPS = (this.OCPS + 1) and 0b10111111
                }
            }
            in 0x8000 until 0xA000 -> {
                vram[currentBank][address - 0x8000] = value and 0xFF
                if (address <= 0x97FF) {
                    updateTile(address - 0x8000)
                }
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Lcd")
        }
    }

    override fun updateTile(address: Int) {
        // get base address for this tile row
        val addr = address and 0x1FFE

        // work out which tile and row was updated
        val tile = addr shr 4 and 511
        val y = addr shr 1 and 7

        var sx: Int
        for (i in 0..7) {

            // find bit index for this pixel
            sx = 1 shl 7 - i

            val lol = (if (vram[currentBank][addr] and sx != 0) 1 else 0) or if (vram[currentBank][addr + 1] and sx != 0) 2 else 0

            tiles[tile + currentBank * 384][y][i] = lol
        }
    }

    override fun renderWindow(row: IntArray) {
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
            var tile = vram[currentBank][mapOffset + lineOffset]

            if (windowTiles && (tile < 128)) {
                tile += 256
            }

            for (i in 0 until 160) {
                color = tiles[tile][y][x]

                /*
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
                }*/

                val info = vram[1][0x1800 + tile]
                val palette = info and 0b00000111
                val banknumber = if (palette.getBit(4)) 1 else 0
                color = tiles[tile + 384*banknumber][y][x]

                windowBuffer[LY][i] = color
                screenBuffer[LY][i * 3] = bgPalettes[palette + color * 2] and 0b00011111
                screenBuffer[LY][i * 3 + 1] = ((bgPalettes[palette + color * 2] and 0b11100000) shr 5) or ((bgPalettes[palette + color * 2 + 1] and 0b00000011) shl 3)
                screenBuffer[LY][i * 3 + 2] = (bgPalettes[palette + color * 2 + 1] and 0b01111100) shr 2

                x++
                if (x == 8) {
                    x = 0
                    lineOffset = (lineOffset + 1) and 31
                    tile = vram[currentBank][mapOffset + lineOffset]
                    if (windowTiles && tile < 128) {
                        tile += 256
                    }
                }
            }
        }
    }

    override fun renderBackground(row: IntArray) {
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
            var tile = vram[currentBank][mapOffset + lineOffset]

            if (backgroundTiles && (tile < 128)) {
                tile += 256
            }

            for (i in 0 until 160) {

                /*
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
                }*/


                val info = vram[1][0x1800 + tile]
                val palette = info and 0b00000111
                val banknumber = if (palette.getBit(4)) 1 else 0
                color = tiles[tile + 384*banknumber][y][x]

                backgroundBuffer[LY][i] = color
                screenBuffer[LY][i * 3] = bgPalettes[palette + color * 2] and 0b00011111
                screenBuffer[LY][i * 3 + 1] = ((bgPalettes[palette + color * 2] and 0b11100000) shr 5) or ((bgPalettes[palette + color * 2 + 1] and 0b00000011) shl 3)
                screenBuffer[LY][i * 3 + 2] = (bgPalettes[palette + color * 2 + 1] and 0b01111100) shr 2

                row[i] = color

                x++
                if (x == 8) {
                    x = 0
                    lineOffset = (lineOffset + 1) and 31
                    tile = vram[currentBank][mapOffset + lineOffset]
                    if (backgroundTiles && tile < 128) {
                        tile += 256
                    }
                }
            }
        }
    }

    override fun renderSprites(row: IntArray) {
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

                    val banknumber = if (obj.vramBank1) 1 else 0
                    val tilerow: IntArray = if (obj.isYFlip) {
                        tiles[temp + banknumber * 384][spriteSize - 1 - spriteRow]
                    } else {
                        tiles[temp + banknumber * 384][spriteRow]
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
                            /*
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
                            }*/

                            screenBuffer[LY][(obj.x + x) * 3] = objPalettes[obj.paletteNumber + color * 2] and 0b00011111
                            screenBuffer[LY][(obj.x + x) * 3 + 1] = ((objPalettes[obj.paletteNumber + color * 2] and 0b11100000) shr 5) or ((objPalettes[obj.paletteNumber + color * 2 + 1] and 0b00000011) shl 3)
                            screenBuffer[LY][(obj.x + x) * 3 + 2] = (objPalettes[obj.paletteNumber + color * 2 + 1] and 0b01111100) shr 2

                            spritesBuffer[LY][obj.x + x] = color
                        }
                    }
                }
            }
        }
    }

    override fun updateSprite(address: Int, value: Int) {
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
                    sprites[spriteNumber].paletteNumber = value and 0b00000111
                    sprites[spriteNumber].vramBank1 = value.getBit(3)
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
}