package cpu.instructions.loads

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

class LD_nn_A(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {
        val value = registers.A

        val address = getWordImmediate()

        mmu.writeByte(address, value)

        return 16
    }
}