package cpu.instructions.shifts

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.getBit

abstract class SRL(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
    protected fun srl(value: Int): Int {
        val carry = value.getBit(0)
        registers.setCFlag(carry)

        val result = (value shr 1)

        val zFlag = (result and 0xFF) == 0
        registers.setZFlag(zFlag)

        registers.setNFlag(false)
        registers.setHFlag(false)

        return result
    }
}