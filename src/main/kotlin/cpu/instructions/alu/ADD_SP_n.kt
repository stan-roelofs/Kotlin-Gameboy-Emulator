package cpu.instructions.alu

import Mmu
import cpu.Registers

class ADD_SP_n(registers: Registers, mmu: Mmu) : ADD(registers, mmu) {

    override fun execute(): Int {

        val value = getImmediate()

        super.add16SP(value)

        return 16
    }
}