package cpu.instructions.loads

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

class LD_SP_HL(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {

        val value = registers.getHL()
        registers.SP = value

        return 8
    }
}