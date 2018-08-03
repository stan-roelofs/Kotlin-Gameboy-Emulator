package cpu.instructions.rotates

import Mmu
import cpu.Registers
import cpu.instructions.Instruction
import getBit
import setBit

abstract class RR(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
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