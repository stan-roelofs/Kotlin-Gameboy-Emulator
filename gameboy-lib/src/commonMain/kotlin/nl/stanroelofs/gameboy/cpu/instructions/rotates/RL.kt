package nl.stanroelofs.gameboy.cpu.instructions.rotates

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.getBit

abstract class RL(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected var value = 0

    override fun reset() {
        super.reset()
        value = 0
    }

    protected fun rl(value: Int): Int {
        val carry = registers.getCFlag()

        var result = (value shl 1)
        if (carry) {
            result += 1
        }

        val cFlag = value.getBit(7)
        registers.setCFlag(cFlag)

        val zFlag = ((result and 0xFF) == 0)
        registers.setZFlag(zFlag)

        registers.setNFlag(false)
        registers.setHFlag(false)

        return result
    }
}