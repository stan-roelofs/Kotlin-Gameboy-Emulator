package cpu.instructions.alu

import Mmu
import cpu.Registers

class SBC_A_n(registers: Registers, mmu: Mmu) : SBC(registers, mmu) {

    override fun execute(): Int {

        val value = getImmediate()

        super.sbc(value)

        return 8
    }
}