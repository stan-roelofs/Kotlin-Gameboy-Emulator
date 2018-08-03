package cpu.instructions.jumps

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

class JR_n(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {
        val value = getImmediate().toByte().toInt()
        registers.PC += value

        return 8
    }
}