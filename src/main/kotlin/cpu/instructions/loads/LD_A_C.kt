package cpu.instructions.loads

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

class LD_A_C(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {
        registers.A = mmu.readByte(registers.C + 0xFF00)
        return 8
    }
}