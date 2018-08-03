package cpu.instructions.alu

import Mmu
import cpu.Registers
import cpu.instructions.Instruction

abstract class ADD(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected fun add8(value: Int) {
        val oldA = registers.A
        registers.A = registers.A + value

        val zFlag = registers.A == 0
        registers.setZFlag(zFlag)

        registers.setNFlag(false)

        val cFlag = oldA + value > 0xFF
        registers.setCFlag(cFlag)

        val hFlag = (oldA and 0xF) + (value and 0xF) > 0xF
        registers.setHFlag(hFlag)
    }

    protected fun add16SP(value: Int) {
        val oldSP = registers.SP
        registers.SP = oldSP + value

        registers.setZFlag(false)

        registers.setNFlag(false)

        val cFlag = oldSP + value > 0xFFFF
        registers.setCFlag(cFlag)

        val hFlag = (oldSP and 0xFFF) + (value and 0xFFF) > 0xFFF
        registers.setHFlag(hFlag)
    }

    protected fun add16HL(value: Int) {
        val temp = registers.getHL() + value

        registers.setNFlag(false)

        val cFlag = (temp > 0xFFFF)
        registers.setCFlag(cFlag)

        val hFlag = (((registers.getHL() and 0xFFF) + (value and 0xFFF)) > 0xFFF)
        registers.setHFlag(hFlag)

        registers.setHL(temp)
    }
}