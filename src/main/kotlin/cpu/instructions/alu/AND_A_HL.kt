package cpu.instructions.alu

import memory.Mmu
import cpu.Registers

class AND_A_HL(registers: Registers, mmu: Mmu) : AND(registers, mmu) {

    override fun execute(): Int {

        val value = mmu.readByte(registers.getHL())

        super.and(value)

        return 8
    }
}