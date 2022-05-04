package nl.stanroelofs.gameboy.cpu.instructions.alu

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu

abstract class ADD(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected var value = 0

    override fun reset() {
        super.reset()
        value = 0
    }

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

    protected fun add8SP(value: Int) {
        val oldSP = registers.SP
        registers.SP = oldSP + value

        registers.setZFlag(false)
        registers.setNFlag(false)

        val cFlag = ((oldSP and 0xFF) + (value and 0xFF)) > 0xFF
        registers.setCFlag(cFlag)

        val hFlag = (oldSP and 0xF) + (value and 0xF) > 0xF
        registers.setHFlag(hFlag)
    }

    protected fun add16HL(value: Int) {
        val temp = registers.HL + value

        registers.setNFlag(false)

        val cFlag = (temp > 0xFFFF)
        registers.setCFlag(cFlag)

        val hFlag = (((registers.HL and 0xFFF) + (value and 0xFFF)) > 0xFFF)
        registers.setHFlag(hFlag)

        registers.HL= temp
    }
}