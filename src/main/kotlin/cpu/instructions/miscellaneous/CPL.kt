package cpu.instructions.miscellaneous

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

class CPL(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override fun execute(): Int {

        var value = registers.A
        value = value.inv()
        value = value and 0xFF
        registers.A = value

        registers.setNFlag(true)
        registers.setHFlag(true)

        return 4
    }
}