package cpu.instructions.loads

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu

class LDI_HL_A(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {

        val HL = registers.getHL()
        mmu.writeByte(HL, registers.A)

        registers.setHL(HL + 1)

        return 8
    }
}