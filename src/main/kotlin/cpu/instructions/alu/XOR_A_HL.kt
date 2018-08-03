package cpu.instructions.alu

import Mmu
import cpu.Registers

class XOR_A_HL(registers: Registers, mmu: Mmu) : XOR(registers, mmu) {

    override fun execute(): Int {
        val value = mmu.readByte(registers.getHL())

        super.xor(value)

        return 8
    }
}