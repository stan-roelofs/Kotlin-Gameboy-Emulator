package cpu.instructions.alu

import Mmu
import cpu.Registers

class XOR_A_n(registers: Registers, mmu: Mmu) : XOR(registers, mmu) {

    override fun execute(): Int {
        val value = getImmediate()

        super.xor(value)

        return 8
    }
}