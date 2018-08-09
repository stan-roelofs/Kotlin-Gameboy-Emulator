package cpu.instructions.alu

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

abstract class INC(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected fun inc(value: Int) {
        registers.setNFlag(false)

        val hFlag = (value and 0xF) + (1 and 0xF) > 0xF
        registers.setHFlag(hFlag)
    }
}