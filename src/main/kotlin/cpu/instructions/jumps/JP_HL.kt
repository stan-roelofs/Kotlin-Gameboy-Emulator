package cpu.instructions.jumps

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class JP_HL(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                val value = registers.getHL()
                registers.PC = value
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}