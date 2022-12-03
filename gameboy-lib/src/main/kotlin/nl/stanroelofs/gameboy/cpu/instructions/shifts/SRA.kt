package nl.stanroelofs.gameboy.cpu.instructions.shifts

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.getBit
import nl.stanroelofs.gameboy.utils.setBit

abstract class SRA(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected var value = 0

    override fun reset() {
        super.reset()
        value = 0
    }

    protected fun sra(value: Int): Int {
        val carry = value.getBit(0)
        registers.setCFlag(carry)

        val msb = value.getBit(7)
        var result = (value shr 1)

        result = result.setBit(7, msb)

        val zFlag = (result and 0xFF) == 0
        registers.setZFlag(zFlag)

        registers.setNFlag(false)
        registers.setHFlag(false)

        return result
    }
}