package cpu.instructions.rotates

import Mmu
import cpu.Registers

class RLA(registers: Registers, mmu: Mmu) : RL(registers, mmu) {
    override fun execute(): Int {
        val value = registers.A
        registers.A = rl(value)
        registers.setZFlag(false)

        return 4
    }
}