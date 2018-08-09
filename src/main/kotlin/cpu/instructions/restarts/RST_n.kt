package cpu.instructions.restarts

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

class RST_n(registers: Registers, mmu: Mmu, private val address: Int) : Instruction(registers, mmu) {
    override fun execute(): Int {
        pushWordToStack(registers.PC)

        registers.PC = address

        return 16
    }
}