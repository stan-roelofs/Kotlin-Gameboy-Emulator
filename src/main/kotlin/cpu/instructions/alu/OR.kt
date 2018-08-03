package cpu.instructions.alu

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

abstract class OR(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected fun or(value: Int) {
        registers.A = registers.A or value

        val zFlag = registers.A == 0
        registers.setZFlag(zFlag)

        registers.setNFlag(false)
        registers.setCFlag(false)
        registers.setHFlag(false)
    }
}