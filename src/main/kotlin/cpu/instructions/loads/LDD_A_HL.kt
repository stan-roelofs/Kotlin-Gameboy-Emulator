package cpu.instructions.loads

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

class LDD_A_HL(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {

        val HL = registers.getHL()
        val value = mmu.readByte(HL)
        registers.setHL(HL - 1)

        registers.A = value

        return 8
    }
}