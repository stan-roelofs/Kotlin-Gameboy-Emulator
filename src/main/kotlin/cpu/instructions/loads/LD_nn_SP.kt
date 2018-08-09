package cpu.instructions.loads

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

class LD_nn_SP(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {

        val value = registers.SP

        val address = getWordImmediate()

        mmu.writeWord(address, value)

        return 20
    }
}