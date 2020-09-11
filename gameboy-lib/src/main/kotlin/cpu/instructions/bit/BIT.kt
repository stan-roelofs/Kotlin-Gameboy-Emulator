package gameboy.cpu.instructions.bit

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu

abstract class BIT(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected var state = false

    override fun reset() {
        super.reset()
        state = false
    }

    protected fun bit(state: Boolean) {
        registers.setZFlag(!state)
        registers.setNFlag(false)
        registers.setHFlag(true)
    }
}