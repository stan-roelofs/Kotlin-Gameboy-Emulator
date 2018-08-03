package cpu.instructions.alu

import Mmu
import cpu.Registers

class ADD_A_n(registers: Registers, mmu: Mmu) : ADD(registers, mmu) {

    override fun execute(): Int {
        val value = getImmediate()

        super.add8(value)

        return 8
    }
}