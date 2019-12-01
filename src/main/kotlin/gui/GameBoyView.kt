package gui

import GameBoy
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.scene.image.WritableImage
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import javafx.util.Duration
import memory.io.Joypad
import tornadofx.*
import java.io.File
import java.util.*

class GameBoyView: View(), Observer {
    private var lcd = WritableImage(320, 288)
    private var oldScreen = Array(144) {IntArray(160)}

    private val gb = GameBoy(null)
    private val vramView = VRamView(gb)
    private val debugView = DebugView(gb)

    val color0 = Color(224f / 255.0, 248f / 255.0, 208f / 255.0, 1.0)
    val color1 = Color(136f / 255.0, 192f / 255.0, 112f / 255.0, 1.0)
    val color2 = Color(52f / 255.0, 104f / 255.0, 86f / 255.0, 1.0)
    val color3 = Color(8f / 255.0, 24f / 255.0, 32f / 255.0, 1.0)
    val colors = arrayOf(color0, color1, color2, color3)

    private var frameDone = false
    private var lastTime = 0L

    private val ticksPerFrame = GameBoy.TICKS_PER_SEC / 60 / 4

    private val tl = Timeline()
    private val play = KeyFrame(Duration.millis(17.0),
            EventHandler {
                /*
                // Keep executing until a frame is ready
                var time = System.currentTimeMillis()
                var kak = 0

                while(!frameDone) {
                    try {
                        gb.step()
                        kak++
                        if (kak > 5000) {
                            kak = 0
                            Thread.sleep(1)
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()
                        tl.stop()
                        break
                    }
                }


                updateVram()
                updateDebug()*/
                frameDone = false
            })

    override val root = gridpane {
        row {
            menubar {
                useMaxWidth = true
                gridpaneConstraints {
                    columnSpan = 4
                }
                menu("File") {
                    item("Load").action {
                        val files = chooseFile("rom", arrayOf(FileChooser.ExtensionFilter("Roms", "*.gb")), FileChooserMode.Single)

                        if (files.isNotEmpty()) {
                            gb.loadCartridge(files[0])

                            tl.stop()
                            tl.keyFrames.remove(0, tl.keyFrames.size)
                            tl.keyFrames.add(play)
                            tl.cycleCount = Animation.INDEFINITE
                            tl.play()

                            Thread(gb).start()

                        }
                    }
                }

                menu("Window") {
                    item("Video RAM / OAM viewer").action {
                        vramView.openWindow()
                    }
                    item("Debug window").action {
                        debugView.openWindow()
                    }
                }
            }
        }
        row {
            imageview(lcd) {
                useMaxWidth = true
                gridpaneConstraints {
                    columnSpan = 4
                }
            }
        }
        row {
            button("Start") {
                action {
                    /*
                    if (tl.status != Animation.Status.RUNNING) {
                        tl.keyFrames.remove(0, tl.keyFrames.size)
                        tl.keyFrames.add(play)
                        tl.cycleCount = Animation.INDEFINITE
                        tl.play()
                    }*/
                    Thread(gb).start()
                }
            }
            button("Stop") {
                action {
                    tl.stop()
                }
            }
            button("Save") {
                action {
                    val cart = gb.mmu.cartridge
                    val path = cart?.cartridgeFile?.parent
                    val fileName = cart?.cartridgeFile?.nameWithoutExtension

                    val saveFile = File(path, "$fileName.sav")
                    cart?.saveRam(saveFile)
                }
            }
            button("Load") {
                action {
                    val cart = gb.mmu.cartridge
                    val path = cart?.cartridgeFile?.parent
                    val fileName = cart?.cartridgeFile?.nameWithoutExtension

                    val loadFile = File(path, "$fileName.sav")

                    if (loadFile.exists() && !loadFile.isDirectory) {
                        cart?.loadRam(loadFile)
                    }
                }
            }
        }
    }

    init {
        gb.mmu.io.lcd.addObserver(this)
        reset()
        registerKeyboard()
    }

    private fun reset() {
        // set initial values to 100 such that the first frame all pixels are forced to redraw
        // TODO: this is just a temporary hack, should be fixed later on
        for (i in 0 until 144) {
            for (j in 0 until 160) {
                oldScreen[i][j] = 100
            }
        }
    }

    private fun registerKeyboard() {
        root.setOnKeyPressed { e ->
            when (e.code) {
                KeyCode.ENTER -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.START)
                KeyCode.LEFT -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.LEFT)
                KeyCode.RIGHT -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.RIGHT)
                KeyCode.UP -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.UP)
                KeyCode.DOWN -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.DOWN)
                KeyCode.Z -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.A)
                KeyCode.X -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.B)
                else -> {}
            }

            e.consume()
        }

        root.setOnKeyReleased { e ->
            when (e.code) {
                KeyCode.ENTER -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.START)
                KeyCode.LEFT -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.LEFT)
                KeyCode.RIGHT -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.RIGHT)
                KeyCode.UP -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.UP)
                KeyCode.DOWN -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.DOWN)
                KeyCode.Z -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.A)
                KeyCode.X -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.B)
                else -> {}
            }

            e.consume()
        }
    }

    private fun updateVram() {
        vramView.update()
    }

    private fun updateDebug() {
        debugView.update()
    }

    override fun update(o: Observable?, arg: Any?) {
        if (frameDone) {
            return
        }
        frameDone = true

        val pixelWriter = lcd.pixelWriter

        @Suppress("UNCHECKED_CAST")
        val screen = arg as Array<IntArray>
        for (y in 0 until 144) {
            for (x in 0 until 160) {
                // If current pixel hasn't changed, skip drawing
                if (oldScreen[y][x] != screen[y][x]) {
                    val c = colors[screen[y][x]]
                    pixelWriter.setColor(2 * x, 2 * y, c)
                    pixelWriter.setColor(2 * x + 1, 2 * y, c)
                    pixelWriter.setColor(2 * x, 2 * y + 1, c)
                    pixelWriter.setColor(2 * x + 1, 2 * y + 1, c)
                    oldScreen[y][x] = screen[y][x]
                }
            }
        }
    }
}

