package cpu.instructions.rotates

import Mmu
import cpu.Registers
import cpu.instructions.Instruction
import getBit

abstract class RLC(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
    protected fun rlc(value: Int): Int {
        val cFlag = value.getBit(7)
        registers.setCFlag(cFlag)

        var result = (value shl 1)
        if (cFlag) {
            result += 1
        }

        val zFlag = (result and 0xFF) == 0
        registers.setZFlag(zFlag)

        registers.setNFlag(false)
        registers.setHFlag(false)

        return result
    }
}