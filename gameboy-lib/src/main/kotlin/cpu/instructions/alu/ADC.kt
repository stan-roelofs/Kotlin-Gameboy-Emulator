package gameboy.cpu.instructions.alu

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu

abstract class ADC(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    protected var value = 0

    override fun reset() {
        super.reset()
        value = 0
    }

    protected fun adc(value: Int) {
        val carry = if (registers.getCFlag()) 1 else 0
        val temp = registers.A + value + carry

        registers.setNFlag(false)

        val hFlag = (((registers.A and 0x0F) + (value and 0x0F) + carry) > 0xF)
        registers.setHFlag(hFlag)

        val cFlag = (temp > 0xFF)
        registers.setCFlag(cFlag)

        val zFlag = ((temp and 0xFF) == 0)
        registers.setZFlag(zFlag)

        registers.A = temp
    }
}