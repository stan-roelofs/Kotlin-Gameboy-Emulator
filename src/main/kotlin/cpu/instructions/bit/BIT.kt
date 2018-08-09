package cpu.instructions.bit

import memory.Mmu
import cpu.Registers
import cpu.instructions.Instruction

abstract class BIT(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected fun bit(state: Boolean) {
        registers.setZFlag(!state)
        registers.setNFlag(false)
        registers.setHFlag(true)
    }
}