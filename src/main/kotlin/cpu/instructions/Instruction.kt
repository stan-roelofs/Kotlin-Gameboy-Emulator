package cpu.instructions

import memory.Mmu
import cpu.Registers
import getFirstByte
import getSecondByte
import setSecondByte

abstract class Instruction(val registers: Registers, val mmu: Mmu) {

    abstract fun execute(): Int

    protected fun getImmediate(): Int {
        val value = mmu.readByte(registers.PC)
        registers.incPC()

        return value
    }

    protected fun getWordImmediate(): Int {
        var value = getImmediate()
        value = setSecondByte(value, getImmediate())

        return value
    }

    protected fun pushToStack(value: Int) {
        registers.decSP()
        mmu.writeByte(registers.SP, value)
    }

    protected fun pushWordToStack(value: Int) {
        pushToStack(value.getSecondByte())
        pushToStack(value.getFirstByte())
    }

    protected fun popFromStack(): Int {
        val value = mmu.readByte(registers.SP)
        registers.incSP()
        return value
    }

    protected fun popWordFromStack(): Int {
        var value = popFromStack()
        value = setSecondByte(value, popFromStack())
        return value
    }
}