import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import javafx.util.Duration
import memory.Mmu
import tornadofx.*
import utils.getBit
import java.io.File

class Gui : App(GameBoyView::class)

class GameBoyView: View() {

    private var regA: Label by singleAssign()
    private var regF: Label by singleAssign()
    private var regB: Label by singleAssign()
    private var regC: Label by singleAssign()
    private var regD: Label by singleAssign()
    private var regE: Label by singleAssign()
    private var regH: Label by singleAssign()
    private var regL: Label by singleAssign()
    private var regSP: Label by singleAssign()
    private var regPC: Label by singleAssign()

    private var lcd = WritableImage(256, 256)
    private var vram = WritableImage(128, 196)

    private var instructions: TextArea by singleAssign()

    //private val gb = GameBoy(File("E:/Downloads/mooneye-gb_hwtests/acceptance/timer/tim11.gb"))
    private val gb = GameBoy(File("E:/Downloads/Tetris/Tetris.gb"))
    //private val gb = GameBoy(File("E:/Downloads/gb-test-roms-master/cpu_instrs/cpu_instrs.gb"))

    val tl = Timeline()
    val play = KeyFrame(Duration.millis(17.0),
            EventHandler {
                while (!gb.mmu.io.lcd.frameDone) {
                    gb.step()
                }
                updateRegisters()
                updateInstructions()
                updateScreen()
                updateVram()

                gb.mmu.io.lcd.frameDone = false
            })

    override val root = gridpane {
        row {
            imageview(lcd) {
                useMaxWidth = true
                gridpaneConstraints {
                    columnSpan = 4
                }
            }
        }
        row {
            imageview(vram) {
                useMaxWidth = true
                gridpaneConstraints {
                    columnSpan = 4
                }
            }
        }
        row {
            label("A")
            regA = label()
            label("F")
            regF = label()

        }
        row {
            label("B")
            regB = label()
            label("C")
            regC = label()
        }
        row {
            label("D")
            regD = label()
            label("E")
            regE = label()
        }
        row {
            label("H")
            regH = label()
            label("L")
            regL = label()
        }
        row {
            label("SP")
            regSP = label()
            label("PC")
            regPC = label()
        }
        row {
            button("Start") {
                action {
                    tl.keyFrames.remove(0, tl.keyFrames.size)
                    tl.cycleCount = Animation.INDEFINITE
                    tl.keyFrames.add(play)
                    tl.play()
                }
            }
            button("Stop") {
                action {
                    tl.stop()
                }
            }
            button("Step") {
                action {
                    gb.step()
                    updateRegisters()
                    updateInstructions()
                    updateScreen()
                    updateVram()
                }
            }
            button("Step 10") {
                action {
                    for (i in 1..10) {
                        gb.step()
                        updateRegisters()
                        updateInstructions()
                        updateScreen()
                        updateVram()
                    }
                }
            }
        }
        row {
            instructions = textarea {
                useMaxWidth = true
                gridpaneConstraints {
                    columnSpan = 4
                    marginTop = 10.0
                }
            }
        }
    }

    init {
        updateRegisters()
    }

    private fun updateVram() {
        val color0 = Color(224f / 255.0, 248f / 255.0, 208f / 255.0, 1.0)
        val color1 = Color(136f / 255.0, 192f / 255.0, 112f / 255.0, 1.0)
        val color2 = Color(52f / 255.0, 104f / 255.0, 86f / 255.0, 1.0)
        val color3 = Color(8f / 255.0, 24f / 255.0, 32f / 255.0, 1.0)
        val colors = arrayOf(color0, color1, color2, color3)

        val pixelWriter = vram.pixelWriter
        for (tiley in 0 until 24) {
            for (tilex in 0 until 16) {
                val addressStart = 0x8000 + tilex * 16 + tiley * 256

                for (y in 0 until 8) {
                    val byte1 = Mmu.instance.readByte(addressStart + y * 2)
                    val byte2 = Mmu.instance.readByte(addressStart + y * 2 + 1)

                    for (x in 0 until 8) {
                        val LSB = if (byte1.getBit(x)) 1 else 0
                        val MSB = if (byte2.getBit(x)) 2 else 0
                        val color = LSB + MSB
                        pixelWriter.setColor(tilex * 8 + (7 - x), tiley * 8 + y, colors[color])
                    }
                }
            }
        }
    }

    private fun updateRegisters() {
        regA.text = Integer.toHexString(gb.cpu.registers.A)
        regF.text = Integer.toHexString(gb.cpu.registers.F)
        regB.text = Integer.toHexString(gb.cpu.registers.B)
        regC.text = Integer.toHexString(gb.cpu.registers.C)
        regD.text = Integer.toHexString(gb.cpu.registers.D)
        regE.text = Integer.toHexString(gb.cpu.registers.E)
        regH.text = Integer.toHexString(gb.cpu.registers.H)
        regL.text = Integer.toHexString(gb.cpu.registers.L)
        regSP.text = Integer.toHexString(gb.cpu.registers.SP)
        regPC.text = Integer.toHexString(gb.cpu.registers.PC)
    }

    private fun updateInstructions() {
        instructions.text = "${instructions.text} ${Integer.toHexString(gb.cpu.lastInstruction)}\n"
    }

    private fun updateScreen() {
        val pixelWriter = lcd.pixelWriter

        for (y in 0 until 256) {
            for (x in 0 until 256) {
                pixelWriter.setColor(x, y, gb.mmu.io.lcd.screen[x][y])
            }
        }
    }
}

