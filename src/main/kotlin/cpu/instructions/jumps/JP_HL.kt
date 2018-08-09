package cpu.instructions.jumps

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

class JP_HL(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {
        val value = registers.getHL()

        registers.PC = value

        return 4
    }
}