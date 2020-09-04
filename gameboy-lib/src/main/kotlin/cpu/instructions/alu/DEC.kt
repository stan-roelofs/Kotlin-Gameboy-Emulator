package cpu.instructions.alu

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu

abstract class DEC(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected var value = 0

    override fun reset() {
        super.reset()
        value = 0
    }

    protected fun dec(value: Int) {
        registers.setNFlag(true)

        val hFlag = (value and 0xF) - (1 and 0xF) < 0
        registers.setHFlag(hFlag)
    }
}