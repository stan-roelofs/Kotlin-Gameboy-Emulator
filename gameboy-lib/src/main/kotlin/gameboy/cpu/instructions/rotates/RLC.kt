package gameboy.cpu.instructions.rotates

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.getBit

abstract class RLC(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected var value = 0

    override fun reset() {
        super.reset()
        value = 0
    }

    protected fun rlc(value: Int): Int {
        val cFlag = value.getBit(7)
        registers.setCFlag(cFlag)

        var result = (value shl 1)
        if (cFlag) {
            result += 1
        }

        val zFlag = (result and 0xFF) == 0
        registers.setZFlag(zFlag)

        registers.setNFlag(false)
        registers.setHFlag(false)

        return result
    }
}