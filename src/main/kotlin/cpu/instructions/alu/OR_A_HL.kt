package cpu.instructions.alu

import memory.Mmu
import cpu.Registers

class OR_A_HL(registers: Registers, mmu: Mmu) : OR(registers, mmu) {

    override fun execute(): Int {

        val value = mmu.readByte(registers.getHL())

        super.or(value)

        return 8
    }
}