package cpu.instructions.bit

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu

abstract class BIT(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected var state = false

    protected fun bit(state: Boolean) {
        registers.setZFlag(!state)
        registers.setNFlag(false)
        registers.setHFlag(true)
    }
}