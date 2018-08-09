package cpu.instructions.alu

import memory.Mmu
import cpu.Registers

class INC_HL(registers: Registers, mmu: Mmu) : INC(registers, mmu) {

    override fun execute(): Int {
        val address = registers.getHL()
        val value = mmu.readByte(address)

        mmu.writeByte(address, value + 1)

        val zFlag = mmu.readByte(registers.getHL()) == 0
        registers.setZFlag(zFlag)

        super.inc(value)

        return 12
    }
}