package cpu.instructions.calls

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log
import utils.getFirstByte
import utils.getSecondByte
import utils.setSecondByte

class CALL_cc_nn(registers: Registers, mmu: Mmu, private val flag: Int, private val state: Boolean) : Instruction(registers, mmu) {

    private var conditionHolds = true
    private var address = 0
    override val totalCycles = 24

    override fun reset() {
        super.reset()
        conditionHolds = true
        address = 0
    }

    override fun tick() {

        when(currentCycle) {
            0 -> {

            }
            4 -> {
                address = getImmediate()
            }
            8 -> {
                address = setSecondByte(address, getImmediate())

                if (!(flag == registers.ZFlag && state && registers.getZFlag()) &&
                        !(flag == registers.ZFlag && !state && !registers.getZFlag()) &&
                        !(flag == registers.CFlag && state && registers.getCFlag()) &&
                        !(flag == registers.CFlag && !state && !registers.getCFlag())) {
                    conditionHolds = false
                }
            }
            12 -> {

            }
            16 -> {
                if (conditionHolds) {
                    pushToStack(registers.PC.getSecondByte())
                }
            }
            20 -> {
                if (conditionHolds) {
                    pushToStack(registers.PC.getFirstByte())
                    registers.PC = address
                }
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }

    override fun isExecuting(): Boolean {
        if ((!conditionHolds && currentCycle == 12) || (conditionHolds && currentCycle == totalCycles)) {
            return false
        }
        return true
    }
}