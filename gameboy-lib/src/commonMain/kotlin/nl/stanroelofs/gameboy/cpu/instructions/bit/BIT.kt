package nl.stanroelofs.gameboy.cpu.instructions.bit

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu

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