package cpu.instructions.rotates

import Mmu
import cpu.Registers

class RRA(registers: Registers, mmu: Mmu) : RR(registers, mmu) {
    override fun execute(): Int {
        val value = registers.A
        registers.A = rr(value)
        registers.setZFlag(false)

        return 4
    }
}