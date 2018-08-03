package cpu.instructions.rotates

import Mmu
import cpu.Registers
import cpu.instructions.Instruction
import getBit

abstract class RL(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
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