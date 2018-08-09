package cpu.instructions.alu

import memory.Mmu
import cpu.Registers

class OR_A_n(registers: Registers, mmu: Mmu) : OR(registers, mmu) {

    override fun execute(): Int {

        val value = getImmediate()

        or(value)

        return 8
    }
}