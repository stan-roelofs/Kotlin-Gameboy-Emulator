package cpu.instructions.loads

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class LD_HL_n(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var value = 0
    override val totalCycles = 12

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                value = getImmediate()
                mmu.writeByte(registers.getHL(), value)
            }
            8 -> {

            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}