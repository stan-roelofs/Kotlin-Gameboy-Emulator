package cpu.instructions.alu

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu

abstract class AND(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected var value = 0

    protected fun and(value: Int) {
        registers.A = registers.A and value

        val zFlag = registers.A == 0
        registers.setZFlag(zFlag)

        registers.setNFlag(false)
        registers.setCFlag(false)
        registers.setHFlag(true)
    }
}