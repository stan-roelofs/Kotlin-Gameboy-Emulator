package cpu.instructions.loads

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class LD_A_C(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {
                registers.A = mmu.readByte(registers.C + 0xFF00)
            }
            4 -> {

            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}