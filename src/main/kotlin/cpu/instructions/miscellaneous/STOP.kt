package cpu.instructions.miscellaneous

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

class STOP(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
    override fun execute(): Int {
        registers.stop = true
        return 4
    }
}