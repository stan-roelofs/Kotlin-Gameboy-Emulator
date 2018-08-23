package gui

import GameBoy
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import tornadofx.*
import utils.Log
import utils.toHexString

class DebugView(private val gb: GameBoy): View() {

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

    private var instructions: TextArea by singleAssign()

    private var memText: TextField by singleAssign()

    override val root = gridpane {
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
            button("Step") {
                action {
                    gb.step()
                    updateRegisters()
                    updateInstructions()
                }
            }
            button("Step 10") {
                action {
                    for (i in 1..10) {
                        gb.step()
                        updateRegisters()
                        updateInstructions()
                    }
                }
            }
        }
        row {
            memText = textfield()
            button("Get memory value") {
                action {
                    Log.d(gb.mmu.readByte(Integer.decode(memText.text)).toHexString())
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

    fun update() {
        updateInstructions()
        updateRegisters()
    }

    private fun updateInstructions() {
        instructions.text = "${instructions.text} ${Integer.toHexString(gb.cpu.lastInstruction)}\n"
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
}