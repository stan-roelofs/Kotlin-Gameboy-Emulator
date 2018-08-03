package cpu.instructions.loads

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

class LD_C_A(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {
        mmu.writeByte(registers.C + 0xFF00, registers.A)
        return 8
    }
}