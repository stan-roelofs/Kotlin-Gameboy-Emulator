package nl.stanroelofs.gameboy.cpu.instructions.alu

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu

abstract class CP(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected var value = 0

    override fun reset() {
        super.reset()
        value = 0
    }

    protected fun cp(value: Int) {
        val zFlag = registers.A == value
        registers.setZFlag(zFlag)

        registers.setNFlag(true)

        val hFlag = (registers.A and 0xF) - (value and 0xF) < 0
        registers.setHFlag(hFlag)

        val cFlag = registers.A < value
        registers.setCFlag(cFlag)
    }
}