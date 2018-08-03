package cpu.instructions.loads

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

class LDH_n_A(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {

        val address = getImmediate() + 0xFF00
        mmu.writeByte(address, registers.A)

        return 12
    }
}