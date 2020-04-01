package gui

import GameBoy
import javafx.scene.control.Label
import javafx.scene.control.TabPane
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import tornadofx.*
import utils.getBit
import utils.toHexString

class VRamView(private val gb: GameBoy): View() {

    val color0 = Color(224f / 255.0, 248f / 255.0, 208f / 255.0, 1.0)
    val color1 = Color(136f / 255.0, 192f / 255.0, 112f / 255.0, 1.0)
    val color2 = Color(52f / 255.0, 104f / 255.0, 86f / 255.0, 1.0)
    val color3 = Color(8f / 255.0, 24f / 255.0, 32f / 255.0, 1.0)
    val colors = arrayOf(color0, color1, color2, color3)

    private val vramImage = WritableImage(256, 392)
    private val windowImage = WritableImage(160, 144)
    private val backgroundImage = WritableImage(160, 144)
    private val spritesImage = WritableImage(160, 144)
    private val oamSprites = Array(40) { WritableImage(32, 32) }
    private val oamLabels = Array(40) { Array<Label?>(4) {null}}
    private val labelVerticalMargin = -2.0

    private var counterVram = 0
    private var counterOam = 0
    private var counterBackground = 0
    private var counterWindow = 0
    private var counterSprites = 0
    private val framesWaitBackground = 60
    private val framesWaitWindow = 60
    private val framesWaitSprites = 60
    private val framesWaitVram = 60
    private val framesWaitOam = 30

    override val root = tabpane {
        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        tab("Tiles") {
            imageview(vramImage)
        }
        tab("OAM") {
            datagrid((0 until 40).toList()) {
                cellHeight = 40.0
                cellWidth = 64.0

                cellCache {
                    hbox {
                        borderpane {
                            center = imageview(oamSprites[it])
                        }

                        vbox {
                            oamLabels[it][0] = label {
                                style {
                                    fontSize = 10.px
                                }
                                vboxConstraints {
                                    marginTopBottom(labelVerticalMargin)
                                }
                            }
                            oamLabels[it][1] = label {
                                style {
                                    fontSize = 10.px
                                }
                                vboxConstraints {
                                    marginTopBottom(labelVerticalMargin)
                                }
                            }
                            oamLabels[it][2] = label {
                                style {
                                    fontSize = 10.px
                                }
                                vboxConstraints {
                                    marginTopBottom(labelVerticalMargin)
                                }
                            }
                            oamLabels[it][3] = label {
                                style {
                                    fontSize = 10.px
                                }
                                vboxConstraints {
                                    marginTopBottom(labelVerticalMargin)
                                }
                            }
                        }
                    }
                }
            }
        }
        tab("Layers") {
            vbox {
                label("Background")
                imageview(backgroundImage)
                label("Window")
                imageview(windowImage)
                //imageview(spritesImage)
            }
        }
    }

    init {
        /*
        // Initialize oam view
        val vb = VBox()
        for (rows in 0 until 5) {
            val hb = HBox()
            for (columns in 0 until 8) {
                val index = rows * 8 + columns

                // Add sprite image
                hb.add(ImageView(oamSprites[index]))

                // Add sprite texts
                val vbText = VBox()

                for (i in 0 until 4) {
                    oamLabels[index][i].vboxConstraints {
                        marginLeft = 5.0
                        marginRight = 20.0
                        marginTopBottom(-3.0)
                    }
                    vbText.add(oamLabels[index][i])
                }
                hb.vboxConstraints {
                    marginTopBottom(5.0)
                }
                hb.add(vbText)
            }
            vb.add(hb)
        }
        root.tabs[1].add(vb)*/
    }

    fun update() {
        updateVram()
        updateOam()
        updateBackground()
        updateWindow()
        //updateSprites()
    }

    private fun updateBackground() {
        if (counterBackground < framesWaitBackground) {
            counterBackground++
            return
        }
        counterBackground = 0

        //val pixels = gb.mmu.io.lcd.getBackgroundPixels()
        val pixelWriter = backgroundImage.pixelWriter
        val pixels = gb.mmu.io.lcd.backgroundBuffer

        for (y in 0 until 144) {
            for (x in 0 until 160) {
               pixelWriter.setColor(x, y, colors[pixels[y][x]])
            }
        }
    }

    private fun updateWindow() {
        if (counterWindow < framesWaitWindow) {
            counterWindow++
            return
        }
        counterWindow = 0

        //val pixels = gb.mmu.io.lcd.getBackgroundPixels()
        val pixelWriter = windowImage.pixelWriter
        val pixels = gb.mmu.io.lcd.windowBuffer

        for (y in 0 until 144) {
            for (x in 0 until 160) {
                pixelWriter.setColor(x, y, colors[pixels[y][x]])
            }
        }
    }

    private fun updateSprites() {
        if (counterSprites < framesWaitSprites) {
            counterSprites++
            return
        }
        counterSprites = 0

        val pixelWriter = spritesImage.pixelWriter
        val pixels = gb.mmu.io.lcd.spritesBuffer

        for (y in 0 until 144) {
            for (x in 0 until 160) {
                pixelWriter.setColor(x, y, colors[pixels[y][x]])
            }
        }
    }

    private fun updateVram() {
        // Only update every framesWait frames
        if (counterVram < framesWaitVram) {
            counterVram++
            return
        }
        counterVram = 0

        // Get pixelWriter
        val pixelWriter = vramImage.pixelWriter

        // Get mmu instance to read memory
        val mmu = gb.mmu

        val screen = Array(128) {IntArray(192)}

        for (tiley in 0 until 24) {
            for (tilex in 0 until 16) {
                val addressStart = 0x8000 + tilex * 16 + tiley * 256

                for (y in 0 until 8) {
                    val byte1 = mmu.readByte(addressStart + y * 2)
                    val byte2 = mmu.readByte(addressStart + y * 2 + 1)

                    for (x in 0 until 8) {
                        val LSB = if (byte1.getBit(x)) 1 else 0
                        val MSB = if (byte2.getBit(x)) 2 else 0
                        val color = LSB + MSB

                        screen[tilex * 8 + (7 - x)][tiley * 8 + y] = color
                    }
                }
            }
        }

        for (x in 0 until 128) {
            for (y in 0 until 192) {
                pixelWriter.setColor(2 * x, 2 * y, colors[screen[x][y]])
                pixelWriter.setColor(2 * x + 1, 2 * y, colors[screen[x][y]])
                pixelWriter.setColor(2 * x, 2 * y + 1, colors[screen[x][y]])
                pixelWriter.setColor(2 * x + 1, 2 * y + 1, colors[screen[x][y]])
            }
        }
    }

    private fun updateOam() {
        // Only update every framesWait frames
        if (counterOam < framesWaitOam) {
            counterOam++
            return
        }
        counterOam = 0

        // Get mmu instance to read memory
        val mmu = gb.mmu

        val screen = Array(40) {Array(8) {IntArray(8)}}

        for (y in 0 until 5) {
            for (x in 0 until 8) {
                val oamAddress = 0xFE00 + x * 4 + y * 32
                val patternY = mmu.readByte(oamAddress)
                val patternX = mmu.readByte(oamAddress + 1)
                val patternNumber = mmu.readByte(oamAddress + 2)
                val patternFlags = mmu.readByte(oamAddress + 3)
                val tileAddress = patternNumber * 16 + 0x8000

                oamLabels[x + y * 8][0]?.text = patternY.toHexString(2)
                oamLabels[x + y * 8][1]?.text = patternX.toHexString(2)
                oamLabels[x + y * 8][2]?.text = patternNumber.toHexString(2)
                oamLabels[x + y * 8][3]?.text = patternFlags.toHexString(2)

                for (yTile in 0 until 8) {
                    val byte1 = mmu.readByte(tileAddress + yTile * 2)
                    val byte2 = mmu.readByte(tileAddress + yTile * 2 + 1)

                    for (xTile in 0 until 8) {
                        val LSB = if (byte1.getBit(xTile)) 1 else 0
                        val MSB = if (byte2.getBit(xTile)) 2 else 0
                        val color = LSB + MSB

                        screen[x + y * 8][(7 - xTile)][yTile] = color
                    }
                }
            }
        }

        for (sprite in 0 until 40) {
            val pixelWriter = oamSprites[sprite].pixelWriter
            for (y in 0 until 8) {
                for (x in 0 until 8) {
                    for (xx in 0 until 4) {
                        for (yy in 0 until 4) {
                            pixelWriter.setColor(4 * x + xx, 4 * y + yy, colors[screen[sprite][x][y]])
                        }
                    }
                }
            }
        }
    }
}