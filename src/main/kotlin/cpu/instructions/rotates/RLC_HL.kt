package cpu.instructions.rotates

import memory.Mmu
import cpu.Registers

class RLC_HL(registers: Registers, mmu: Mmu) : RLC(registers, mmu) {

    override fun execute(): Int {
        val address = registers.getHL()
        val value = mmu.readByte(address)
        mmu.writeByte(address, rlc(value))

        return 16
    }
}