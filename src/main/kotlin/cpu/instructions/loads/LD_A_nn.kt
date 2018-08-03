package cpu.instructions.loads

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

class LD_A_nn(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {
        val value = getWordImmediate()

        registers.A = mmu.readByte(value)

        return 16
    }
}