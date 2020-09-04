package cpu.instructions.alu

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu

abstract class SUB(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected var value = 0

    override fun reset() {
        super.reset()
        value = 0
    }

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