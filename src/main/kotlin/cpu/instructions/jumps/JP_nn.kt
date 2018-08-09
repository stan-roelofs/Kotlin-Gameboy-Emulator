package cpu.instructions.jumps

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

class JP_nn(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {
        val value = getWordImmediate()

        registers.PC = value

        return 16
    }
}