package cpu.instructions.rotates

import memory.Mmu
import cpu.Registers

class RR_HL(registers: Registers, mmu: Mmu) : RR(registers, mmu) {

    override fun execute(): Int {
        val address = registers.getHL()
        val value = mmu.readByte(address)
        mmu.writeByte(address, rr(value))

        return 16
    }
}