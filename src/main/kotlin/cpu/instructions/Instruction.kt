package cpu.instructions

import cpu.Registers
import memory.Mmu
import utils.Log
import utils.getFirstByte
import utils.getSecondByte
import utils.setSecondByte

abstract class Instruction(val registers: Registers, val mmu: Mmu) {

    abstract val totalCycles: Int
    protected var currentCycle: Int = 0

    abstract fun tick()

    protected fun getSignedImmediate(): Int {
        val value = mmu.readByte(registers.PC)
        registers.incPC()

        return value.toByte().toInt()
    }

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

    open fun isExecuting(): Boolean {
        if (currentCycle > totalCycles) {
            Log.e("CurrentCycle > totalCycles") // TODO; remove this
        }

        return this.currentCycle == totalCycles
    }
}