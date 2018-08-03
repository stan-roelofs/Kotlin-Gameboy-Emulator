package cpu.instructions.rotates

import Mmu
import cpu.Registers

class RRCA(registers: Registers, mmu: Mmu) : RRC(registers, mmu) {
    override fun execute(): Int {
        val value = registers.A
        registers.A = rrc(value)
        registers.setZFlag(false)

        return 4
    }
}