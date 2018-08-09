package cpu.instructions.alu

import memory.Mmu
import cpu.Registers

class CP_A_n(registers: Registers, mmu: Mmu) : CP(registers, mmu) {

    override fun execute(): Int {

        val value = getImmediate()

        super.cp(value)

        return 8
    }
}