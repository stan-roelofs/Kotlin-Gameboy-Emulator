package cpu.instructions.loads

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class LD_C_A(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {
                mmu.writeByte(registers.C + 0xFF00, registers.A)
            }
            4 -> {

            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}