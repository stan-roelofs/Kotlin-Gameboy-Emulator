package cpu.instructions.miscellaneous

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

class EI(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
    override fun execute(): Int {
        return 4
    }
}