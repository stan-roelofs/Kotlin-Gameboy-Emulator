package nl.stanroelofs.gameboy.cpu.instructions

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.getFirstByte
import nl.stanroelofs.gameboy.utils.getSecondByte
import nl.stanroelofs.gameboy.utils.setSecondByte

abstract class Instruction(val registers: Registers, val mmu: Mmu) {

    abstract val totalCycles: Int
    protected var currentCycle: Int = 0

    open fun reset() {
        currentCycle = 0
    }

    abstract fun tick()

    protected fun getSignedImmediate(): Int {
        return getImmediate().toByte().toInt()
    }

    protected fun getImmediate(): Int {
        val value = mmu.readByte(registers.PC)
        registers.incPC()

        return value
    }

    protected fun getWordImmediate(): Int {
        var value = getImmediate()
        value = value.setSecondByte(getImmediate())

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
        value = value.setSecondByte(popFromStack())
        return value
    }

    open fun isExecuting(): Boolean {
        return this.currentCycle < totalCycles
    }
}