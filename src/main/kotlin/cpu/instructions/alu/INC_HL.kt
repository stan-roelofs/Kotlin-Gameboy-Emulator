package cpu.instructions.alu

import cpu.Registers
import memory.Mmu

class INC_HL(registers: Registers, mmu: Mmu) : INC(registers, mmu) {

    override fun execute(): Int {
        val address = registers.getHL()
        val value = mmu.readByte(address)

        mmu.writeByte(address, value + 1)

        val zFlag = mmu.readByte(address) == 0
        registers.setZFlag(zFlag)

        super.inc(value)

        return 12
    }
}