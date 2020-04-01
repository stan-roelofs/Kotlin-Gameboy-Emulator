package gui

import GameBoy
import javafx.application.Platform
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
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

    private var gbThread : Thread? = null
    private val gb = GameBoy(null)
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

    override val root = vbox {
        menubar {
            menu("File") {
                item("Open rom").action {
                    val file = romChooser.chooseRom(null)

                    gb.stop()
                    gbThread?.join()

                    if (file != null) {
                        gb.loadCartridge(file)
                        cartridgeView.update()
                        gbThread = Thread(gb)
                        gbThread?.start()
                    }
                }
            }

            menu("System") {
                item("Toggle pause").action {
                    gb.togglePause()
                }
                separator()
                item("Load ").action {
                    val cart = gb.mmu.cartridge
                    val path = cart?.cartridgeFile?.parent
                    val fileName = cart?.cartridgeFile?.nameWithoutExtension

                    val loadFile = File(path, "$fileName.sav")

                    if (loadFile.exists() && !loadFile.isDirectory) {
                        cart?.loadRam(loadFile)
                    }
                }
                item("Save").action {
                    val cart = gb.mmu.cartridge
                    val path = cart?.cartridgeFile?.parent
                    val fileName = cart?.cartridgeFile?.nameWithoutExtension

                    val saveFile = File(path, "$fileName.sav")
                    cart?.saveRam(saveFile)
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
                separator()
                checkmenuitem("Sound channel 1") {
                    isSelected = true

                    action {
                        gb.mmu.io.sound.optionChannelEnables[0] = isSelected
                    }
                }
                checkmenuitem("Sound channel 2") {
                    isSelected = true

                    action {
                        gb.mmu.io.sound.optionChannelEnables[1] = isSelected
                    }
                }
                checkmenuitem("Sound channel 3") {
                    isSelected = true

                    action {
                        gb.mmu.io.sound.optionChannelEnables[2] = isSelected
                    }
                }
                checkmenuitem("Sound channel 4") {
                    isSelected = true

                    action {
                        gb.mmu.io.sound.optionChannelEnables[3] = isSelected
                    }
                }
            }
        }

        imageViewLcd = imageview(lcd)
    }

    init {
        primaryStage.titleProperty().unbind()
        primaryStage.setOnCloseRequest {
            gb.stop()
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
        whenDocked {
            root.scene.addEventFilter(KeyEvent.KEY_PRESSED) { e ->
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

            root.scene.addEventFilter(KeyEvent.KEY_RELEASED) { e ->
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
    }

    private fun updateVram() {
        vramView.update()
    }

    private fun updateDebug() {
        debugView.update()
    }

    override fun update(o: Observable?, arg: Any?) {
        Platform.runLater {
            frameCount++

            val currentTime = System.currentTimeMillis()
            if (currentTime - prevTime >= 1000) {
                title = "KGB - FPS: $frameCount"
                prevTime = currentTime
                frameCount = 0
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

            updateDebug()
            updateVram()
        }
    }
}

