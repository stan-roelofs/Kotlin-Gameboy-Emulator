package cpu.instructions.alu

import memory.Mmu
import cpu.Registers

class AND_A_n(registers: Registers, mmu: Mmu) : AND(registers, mmu) {

    override fun execute(): Int {
        val value = getImmediate()

        super.and(value)

        return 8
    }
}