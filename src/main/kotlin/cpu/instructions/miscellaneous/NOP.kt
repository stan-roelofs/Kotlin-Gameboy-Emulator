package cpu.instructions.miscellaneous

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

class NOP(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
    override fun execute(): Int {
        return 4
    }
}