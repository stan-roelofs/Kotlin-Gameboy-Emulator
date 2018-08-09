package cpu.instructions.alu

import memory.Mmu
import cpu.Registers

class ADD_A_HL(registers: Registers, mmu: Mmu) : ADD(registers, mmu) {

    override fun execute(): Int {

        val value = mmu.readByte(registers.getHL())

        super.add8(value)

        return 8
    }
}