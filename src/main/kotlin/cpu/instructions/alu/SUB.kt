package cpu.instructions.alu

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

abstract class SUB(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected fun sub(value: Int) {
        val oldA = registers.A
        registers.A = registers.A - value

        val zFlag = registers.A == 0
        registers.setZFlag(zFlag)

        registers.setNFlag(true)

        val cFlag = oldA - value < 0
        registers.setCFlag(cFlag)

        val hFlag = (oldA and 0xF) - (value and 0xF) < 0
        registers.setHFlag(hFlag)
    }
}