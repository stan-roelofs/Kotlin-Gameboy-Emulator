package cpu.instructions.miscellaneous

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class DI(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                registers.IME = false
                registers.eiExecuted = false // Clear this flag in case EI was executed last cycle
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}