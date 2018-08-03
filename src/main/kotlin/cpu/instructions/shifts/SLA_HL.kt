package cpu.instructions.shifts

import Mmu
import cpu.Registers

class SLA_HL(registers: Registers, mmu: Mmu) : SLA(registers, mmu) {

    override fun execute(): Int {
        val address = registers.getHL()
        val value = mmu.readByte(address)
        mmu.writeByte(address, sla(value))

        return 16
    }
}