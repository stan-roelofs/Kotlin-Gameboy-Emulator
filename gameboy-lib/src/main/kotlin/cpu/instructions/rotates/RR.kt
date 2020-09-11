package gameboy.cpu.instructions.rotates

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.getBit
import gameboy.utils.setBit

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
            result = setBit(result, 7, true)
        }

        registers.setCFlag(carry)

        val zFlag = (result and 0xFF) == 0
        registers.setZFlag(zFlag)

        registers.setNFlag(false)
        registers.setHFlag(false)

        return result
    }
}