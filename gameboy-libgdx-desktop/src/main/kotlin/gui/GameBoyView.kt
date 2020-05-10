package gui

import GameboyDesktop
import GameboyLibgdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import gameboy.GameBoy
import javafx.application.Platform
import tornadofx.*
import java.io.File

class GameBoyView: View() {
    private var scale = 2
    set(value) {
        field = if (value <= 0) {
            1
        } else {
            value
        }
    }
    private val romChooser = RomChooser()
    private val gb = GameBoy(null)
    private val vramView = VRamView(gb)
    private val debugView = DebugView(gb)
    private val cartridgeView = CartridgeView(gb)
    private var gbapp = GameboyDesktop(gb)

    override val root = vbox {
        menubar {
            menu("File") {
                item("Open rom").action {
                    val file = romChooser.chooseRom(null)

                    gbapp.stopgb()

                    if (file != null) {
                        gb.loadCartridge(file)
                        cartridgeView.update()
                        gbapp.startgb()
                    }
                }
            }

            menu("System") {
                item("Toggle pause").action {
                    gb.togglePause()
                }
                separator()
                item("Load ").action {
                    val cart = gb.cartridge
                    val path = cart?.cartridgeFile?.parent
                    val fileName = cart?.cartridgeFile?.nameWithoutExtension

                    val loadFile = File(path, "$fileName.sav")

                    if (loadFile.exists() && !loadFile.isDirectory) {
                        cart?.loadRam(loadFile)
                    }
                }
                item("Save").action {
                    val cart = gb.cartridge
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

                }
                item("Decrease lcd scale").action {
                    scale--
                    val width = 160 * scale
                    val height = 144 * scale

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
    }

    init {
        primaryStage.setOnCloseRequest {
            gb.stop()
            Platform.exit()
            System.exit(0)
        }

        val cfg = LwjglApplicationConfiguration()
        cfg.title = "Gameboy"
        cfg.width = 320
        cfg.height = 288
        LwjglApplication(gbapp, cfg)
    }

    private fun updateVram() {
        vramView.update()
    }

    private fun updateDebug() {
        debugView.update()
    }
}

