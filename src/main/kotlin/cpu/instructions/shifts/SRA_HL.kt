package cpu.instructions.shifts

import Mmu
import cpu.Registers

class SRA_HL(registers: Registers, mmu: Mmu) : SRA(registers, mmu) {

    override fun execute(): Int {
        val address = registers.getHL()
        val value = mmu.readByte(address)
        mmu.writeByte(address, sra(value))

        return 16
    }
}