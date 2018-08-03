package cpu.instructions.shifts

import Mmu
import cpu.Registers
import cpu.instructions.Instruction
import getBit

abstract class SLA(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {
    protected fun sla(value: Int): Int {
        val carry = value.getBit(7)
        registers.setCFlag(carry)

        val result = (value shl 1)

        val zFlag = (result and 0xFF) == 0
        registers.setZFlag(zFlag)

        registers.setNFlag(false)
        registers.setHFlag(false)

        return result
    }
}