package nl.stanroelofs.gameboy.cpu.instructions.alu

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu

abstract class INC(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected var value = 0

    override fun reset() {
        super.reset()
        value = 0
    }

    protected fun inc(value: Int) {
        registers.setNFlag(false)

        val hFlag = (value and 0xF) + (1 and 0xF) > 0xF
        registers.setHFlag(hFlag)
    }
}