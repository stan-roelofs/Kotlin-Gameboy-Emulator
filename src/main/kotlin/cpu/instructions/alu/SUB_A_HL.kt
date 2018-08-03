package cpu.instructions.alu

import Mmu
import cpu.Registers

class SUB_A_HL(registers: Registers, mmu: Mmu) : SUB(registers, mmu) {

    override fun execute(): Int {

        val value = mmu.readByte(registers.getHL())

        super.sub(value)

        return 8
    }
}