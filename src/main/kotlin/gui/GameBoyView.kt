package gui

import GameBoy
import javafx.application.Platform
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import memory.io.Joypad
import memory.io.sound.SoundOutput
import tornadofx.*
import java.io.File
import java.util.*

class GameBoyView: View(), Observer {
    private var scale = 2
    set(value) {
        field = if (value <= 0) {
            1
        } else {
            value
        }
    }
    private lateinit var imageViewLcd: ImageView
    private var lcd = WritableImage(320, 288)
    private var oldScreen = Array(144) {IntArray(160)}
    private val romChooser = RomChooser()

    private val gb = GameBoy(null)
    private val gbThread = Thread(gb)
    private val vramView = VRamView(gb)
    private val debugView = DebugView(gb)
    private val cartridgeView = CartridgeView(gb)

    val color0 = Color(224f / 255.0, 248f / 255.0, 208f / 255.0, 1.0)
    val color1 = Color(136f / 255.0, 192f / 255.0, 112f / 255.0, 1.0)
    val color2 = Color(52f / 255.0, 104f / 255.0, 86f / 255.0, 1.0)
    val color3 = Color(8f / 255.0, 24f / 255.0, 32f / 255.0, 1.0)
    val colors = arrayOf(color0, color1, color2, color3)

    private var forceRefresh = false
    private var prevTime = 0L
    private var frameCount = 0
    private lateinit var labelFps: Label

    override val root = gridpane {
        row {
            menubar {
                useMaxWidth = true
                gridpaneConstraints {
                    columnSpan = 5
                }
                menu("File") {
                    item("Load").action {
                        val file = romChooser.chooseRom(null)

                        if (file != null) {
                            gb.loadCartridge(file)
                            debugView.update()
                            gbThread.start()
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
                    item("Cartridge information").action {
                        cartridgeView.openWindow()
                    }
                }

                menu("Settings") {
                    item("Increase lcd scale").action {
                        scale++
                        val width = 160 * scale
                        val height = 144 * scale
                        lcd = WritableImage(width, height)
                        imageViewLcd.image = lcd
                        forceRefresh = true
                        primaryStage.sizeToScene()
                    }
                    item("Decrease lcd scale").action {
                        scale--
                        val width = 160 * scale
                        val height = 144 * scale
                        lcd = WritableImage(width, height)
                        imageViewLcd.image = lcd
                        forceRefresh = true
                        primaryStage.sizeToScene()
                    }
                }
            }
        }
        row {
            imageViewLcd = imageview(lcd) {
                useMaxWidth = true
                gridpaneConstraints {
                    columnSpan = 5
                }
            }
        }
        row {
            button("Start") {
                action {
                    gbThread.start()
                }
            }
            button("Stop") {
                action {
                   // gbThread.stop()
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
            labelFps = label("FPS: ") {

            }
        }
    }

    init {
        primaryStage.setOnCloseRequest {
            gb.running = false
            Platform.exit()
            System.exit(0)
        }

        gb.mmu.io.lcd.addObserver(this)
        gb.mmu.io.sound.output = SoundOutput()
        reset()
        registerKeyboard()
        forceRefresh = true
    }

    private fun reset() {
        forceRefresh = true
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
                KeyCode.TAB -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.SELECT)
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
                KeyCode.TAB -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.SELECT)
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
        frameCount++

        val currentTime = System.currentTimeMillis()
        if (currentTime - prevTime >= 1000) {
            Platform.runLater {
                labelFps.text = "FPS: $frameCount"
                prevTime = currentTime
                frameCount = 0
            }
        }

        val pixelWriter = lcd.pixelWriter

        val screen = arg as Array<IntArray>
        for (y in 0 until 144) {
            for (x in 0 until 160) {
                // If current pixel hasn't changed, skip drawing
                if (forceRefresh || oldScreen[y][x] != screen[y][x]) {
                    val c = colors[screen[y][x]]

                    for (i in 0 until scale) {
                        for (j in 0 until scale) {
                            pixelWriter.setColor(scale * x + i, scale * y + j, c)
                        }
                    }

                    oldScreen[y][x] = screen[y][x]
                }
            }
        }

        if (forceRefresh) {
            forceRefresh = false
        }

        Platform.runLater {
            updateDebug()
            updateVram()
        }
    }
}

