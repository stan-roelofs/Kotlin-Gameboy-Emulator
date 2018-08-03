package cpu.instructions.alu

import Mmu
import cpu.Registers

class SBC_A_HL(registers: Registers, mmu: Mmu) : SBC(registers, mmu) {

    override fun execute(): Int {

        val value = mmu.readByte(registers.getHL())

        super.sbc(value)

        return 8
    }
}