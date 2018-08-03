package cpu.instructions.rotates

import Mmu
import cpu.Registers

class RLCA(registers: Registers, mmu: Mmu) : RLC(registers, mmu) {
    override fun execute(): Int {
        val value = registers.A
        registers.A = rlc(value)
        registers.setZFlag(false)

        return 4
    }
}