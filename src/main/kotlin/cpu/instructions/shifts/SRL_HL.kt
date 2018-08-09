package cpu.instructions.shifts

import memory.Mmu
import cpu.Registers

class SRL_HL(registers: Registers, mmu: Mmu) : SRL(registers, mmu) {

    override fun execute(): Int {
        val address = registers.getHL()
        val value = mmu.readByte(address)
        mmu.writeByte(address, srl(value))

        return 16
    }
}