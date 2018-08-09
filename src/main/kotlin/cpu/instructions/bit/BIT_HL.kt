package cpu.instructions.bit

import memory.Mmu
import cpu.Registers
import getBit

class BIT_HL(registers: Registers, mmu: Mmu, private val index: Int) : BIT(registers, mmu) {

    override fun execute(): Int {

        val address = registers.getHL()
        val state = mmu.readByte(address).getBit(index)

        super.bit(state)

        return 16
    }
}