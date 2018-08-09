package cpu.instructions.alu

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

abstract class DEC(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected fun dec(value: Int) {
        registers.setNFlag(true)

        val hFlag = (value and 0xF) - (1 and 0xF) < 0
        registers.setHFlag(hFlag)
    }
}