package cpu.instructions.alu

import memory.Mmu
import cpu.Registers

class SUB_A_n(registers: Registers, mmu: Mmu) : SUB(registers, mmu) {

    override fun execute(): Int {

        val value = getImmediate()

        super.sub(value)

        return 8
    }
}