package cpu.instructions.shifts

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.getBit
import utils.setBit

abstract class SRA(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
    protected fun sra(value: Int): Int {
        val carry = value.getBit(0)
        registers.setCFlag(carry)

        val msb = value.getBit(7)
        var result = (value shr 1)

        result = setBit(result, 7, msb)

        val zFlag = (result and 0xFF) == 0
        registers.setZFlag(zFlag)

        registers.setNFlag(false)
        registers.setHFlag(false)

        return result
    }
}