package cpu.instructions.loads

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

class LDH_A_n(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {

        val address = getImmediate() + 0xFF00

        registers.A = mmu.readByte(address)

        return 12
    }
}