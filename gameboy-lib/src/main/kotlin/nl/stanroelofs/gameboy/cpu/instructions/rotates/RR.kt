package nl.stanroelofs.gameboy.cpu.instructions.rotates

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.getBit
import nl.stanroelofs.gameboy.utils.setBit

abstract class RR(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected var value = 0

    override fun reset() {
        super.reset()
        value = 0
    }

    protected fun rr(value: Int): Int {
        val carry = value.getBit(0)
        val cFlag = registers.getCFlag()

        var result = (value shr 1)
        if (cFlag) {
            result = result.setBit(7, true)
        }

        registers.setCFlag(carry)

        val zFlag = (result and 0xFF) == 0
        registers.setZFlag(zFlag)

        registers.setNFlag(false)
        registers.setHFlag(false)

        return result
    }
}