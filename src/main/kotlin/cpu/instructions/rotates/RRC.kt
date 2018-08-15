package cpu.instructions.rotates

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.getBit
import utils.setBit

abstract class RRC(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
    protected fun rrc(value: Int): Int {
        val carry = value.getBit(0)
        registers.setCFlag(carry)

        var result = (value shr 1)
        if (carry) {
            result = setBit(result, 7, true)
        }

        val zFlag = (result and 0xFF) == 0
        registers.setZFlag(zFlag)

        registers.setNFlag(false)
        registers.setHFlag(false)

        return result
    }
}