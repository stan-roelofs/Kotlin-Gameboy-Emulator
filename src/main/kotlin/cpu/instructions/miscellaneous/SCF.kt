package cpu.instructions.miscellaneous

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

class SCF(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
    override fun execute(): Int {
        registers.setNFlag(false)
        registers.setHFlag(false)
        registers.setCFlag(true)
        return 4
    }
}