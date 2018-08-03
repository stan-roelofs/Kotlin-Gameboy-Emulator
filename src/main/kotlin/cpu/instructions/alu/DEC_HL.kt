package cpu.instructions.alu

import Mmu
import cpu.Registers

class DEC_HL(registers: Registers, mmu: Mmu) : DEC(registers, mmu) {

    override fun execute(): Int {
        val address = registers.getHL()
        val value = mmu.readByte(address)

        mmu.writeByte(address, value - 1)

        val zFlag = registers.getHL() == 0
        registers.setZFlag(zFlag)

        super.dec(value)

        return 12
    }
}