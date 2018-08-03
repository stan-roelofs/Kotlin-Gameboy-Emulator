package cpu.instructions.loads

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

class LD_HL_n(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {

        mmu.writeByte(registers.getHL(), getImmediate())

        return 12
    }
}