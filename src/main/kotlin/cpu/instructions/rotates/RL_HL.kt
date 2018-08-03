package cpu.instructions.rotates

import Mmu
import cpu.Registers

class RL_HL(registers: Registers, mmu: Mmu) : RL(registers, mmu) {

    override fun execute(): Int {
        val address = registers.getHL()
        val value = mmu.readByte(address)
        mmu.writeByte(address, rl(value))

        return 16
    }
}