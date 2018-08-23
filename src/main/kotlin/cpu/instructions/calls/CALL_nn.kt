package cpu.instructions.calls

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu

class CALL_nn(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
    override fun execute(): Int {
        val address = getWordImmediate()
        pushWordToStack(registers.PC)
        registers.PC = address

        return 24
    }
}