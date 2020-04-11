package cpu.instructions.jumps

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log
import utils.setSecondByte

class JP_cc_nn(registers: Registers, mmu: Mmu, private val flag: Int, private val state: Boolean) : Instruction(registers, mmu) {

    private var value = 0
    private var conditionHolds = true
    override val totalCycles = 16

    override fun tick() {

        when(currentCycle) {
            0 -> {

            }
            4 -> {
                value = getImmediate()
            }
            8 -> {
                value = setSecondByte(value, getImmediate())

                if (!(flag == registers.ZFlag && state && registers.getZFlag()) &&
                        !(flag == registers.ZFlag && !state && !registers.getZFlag()) &&
                        !(flag == registers.CFlag && state && registers.getCFlag()) &&
                        !(flag == registers.CFlag && !state && !registers.getCFlag())) {
                    conditionHolds = false
                }
            }
            12 -> {
                if (conditionHolds) {
                    registers.PC = value
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