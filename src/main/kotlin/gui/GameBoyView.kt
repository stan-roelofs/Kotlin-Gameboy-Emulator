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
import memory.IO.Joypad
import tornadofx.*
import java.io.File

class GameBoyView: View() {
    private var lcd = WritableImage(512, 512)
    private var oldScreen = Array(256) {IntArray(256)}

    //private val gb = GameBoy(File("E:/Downloads/mooneye-gb_hwtests/acceptance/timer/tim11.gb"))
    private val gb = GameBoy(File("E:/Downloads/mario/drmario.gb"))
    //private val gb = GameBoy(File("E:/Downloads/gb-test-roms-master/cpu_instrs/cpu_instrs.gb"))
    private val vramView = VRamView(gb)
    private val debugView = DebugView(gb)

    val color0 = Color(224f / 255.0, 248f / 255.0, 208f / 255.0, 1.0)
    val color1 = Color(136f / 255.0, 192f / 255.0, 112f / 255.0, 1.0)
    val color2 = Color(52f / 255.0, 104f / 255.0, 86f / 255.0, 1.0)
    val color3 = Color(8f / 255.0, 24f / 255.0, 32f / 255.0, 1.0)
    val colors = arrayOf(color0, color1, color2, color3)

    val tl = Timeline()
    val play = KeyFrame(Duration.millis(17.0),
            EventHandler {
                gb.frame()
                updateScreen()
                updateVram()
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
                        }
                    }
                }

                menu("Window") {
                    item("Video RAM window").action {
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
                    tl.keyFrames.remove(0, tl.keyFrames.size)
                    tl.keyFrames.add(play)
                    tl.cycleCount = Animation.INDEFINITE
                    tl.play()
                }
            }
            button("Stop") {
                action {
                    tl.stop()
                }
            }
        }
    }

    init {
        // set initial values to 100 such that the first frame all pixels are forced to redraw
        // TODO might wanna change this implementation
        for (i in 0..255) {
            for (j in 0..255) {
                oldScreen[i][j] = 100
            }
        }
        registerKeyboard()

        tl.keyFrames.remove(0, tl.keyFrames.size)
        tl.keyFrames.add(play)
        tl.cycleCount = Animation.INDEFINITE
        tl.play()
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

    private fun updateScreen() {
        val pixelWriter = lcd.pixelWriter

        for (y in 0 until 256) {
            for (x in 0 until 256) {
                if (oldScreen[x][y] != gb.mmu.io.lcd.screen[x][y]) {
                    pixelWriter.setColor(2 * x, 2 * y, colors[gb.mmu.io.lcd.screen[x][y]])
                    pixelWriter.setColor(2 * x + 1, 2 * y, colors[gb.mmu.io.lcd.screen[x][y]])
                    pixelWriter.setColor(2 * x, 2 * y + 1, colors[gb.mmu.io.lcd.screen[x][y]])
                    pixelWriter.setColor(2 * x + 1, 2 * y + 1, colors[gb.mmu.io.lcd.screen[x][y]])
                    oldScreen[x][y] = gb.mmu.io.lcd.screen[x][y]
                }
            }
        }
    }
}

