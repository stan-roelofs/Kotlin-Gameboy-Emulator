import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import tornadofx.*
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

    private var lcd = WritableImage(166, 144)
    private val colors = arrayOf(Color.rgb(255, 255, 255), Color.rgb(170, 170, 170), Color.rgb(85, 85, 85), Color.rgb(0, 0, 0))

    private var instructions: TextArea by singleAssign()

    private val gb = GameBoy(File("E:/Downloads/gb-test-roms-master/cpu_instrs/individual/01-special.gb"))

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
            button("Next") {
                action {
                    var i = 0
                    val f = File("C:/Users/Stan/Desktop/lol.txt").bufferedWriter()
                    //while (i < 250000) {
/*
                        var test = String.format("%04X", gb.cpu.registers.PC) + ":"
                        test += "   A:" + String.format("%02x", gb.cpu.registers.A)
                        test += "    B:" + String.format("%02x", gb.cpu.registers.B)
                        test += "    C:" + String.format("%02x", gb.cpu.registers.C)
                        test += "    D:" + String.format("%02x", gb.cpu.registers.D)
                        test += "    E:" + String.format("%02x", gb.cpu.registers.E)
                        test += "    F:" + String.format("%02x", gb.cpu.registers.F)
                        test += "    H:" + String.format("%02x", gb.cpu.registers.H)
                        test += "    L:" + String.format("%02x", gb.cpu.registers.L)
                        test += "    SP:" + String.format("%02x", gb.cpu.registers.SP)

                        f.write(test)
                        f.newLine()
*/
                        gb.step()
                        i++
                    //}
                    f.close()

                }
            }
            button("Frame") {
                action {
                    for (i in 1..100) {
                        while (!gb.gpu.frameDone) {
                            gb.step()
                        }
                        updateRegisters()
                        updateInstructions()
                        updateScreen()
                        gb.gpu.frameDone = false
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

        for (y in 0 until 140) {
            for (x in 0 until 160) {
                pixelWriter.setColor(x, y, colors[gb.gpu.screen[y][x]])
                pixelWriter.setColor(x, y + 1, colors[gb.gpu.screen[y][x]])
                pixelWriter.setColor(x + 1, y, colors[gb.gpu.screen[y][x]])
                pixelWriter.setColor(x + 1, y + 1, colors[gb.gpu.screen[y][x]])
            }
        }
    }
}

