package cpu.instructions.returns

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

class RETI(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
    override fun execute(): Int {
        val address = popWordFromStack()

        registers.PC = address

        return 16
    }
}