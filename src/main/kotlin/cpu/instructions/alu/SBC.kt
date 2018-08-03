package cpu.instructions.alu

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

abstract class SBC(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected fun sbc(value: Int) {
        val carry = if (registers.getCFlag()) 1 else 0
        val temp = registers.A - value - carry

        registers.setNFlag(true)

        val hFlag = (((registers.A and 0x0F) - (value and 0x0F) - carry) < 0)
        registers.setHFlag(hFlag)

        val cFlag = (temp < 0)
        registers.setCFlag(cFlag)

        val zFlag = ((temp and 0xFF) == 0)
        registers.setZFlag(zFlag)

        registers.A = temp
    }
}