package gameboy.cpu.instructions.rotates

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.getBit
import gameboy.utils.setBit

abstract class RRC(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected var value = 0

    override fun reset() {
        super.reset()
        value = 0
    }

    protected fun rrc(value: Int): Int {
        val carry = value.getBit(0)
        registers.setCFlag(carry)

        var result = (value shr 1)
        if (carry) {
            result = setBit(result, 7, true)
        }

        val zFlag = (result and 0xFF) == 0
        registers.setZFlag(zFlag)

        registers.setNFlag(false)
        registers.setHFlag(false)

        return result
    }
}