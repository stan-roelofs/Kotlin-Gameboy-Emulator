package gameboy.cpu.instructions.alu

import gameboy.cpu.Registers
import gameboy.memory.Mmu

class ADC_A_HL(registers: Registers, mmu: Mmu) : ADC(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {
            }
            4 -> {
                value = mmu.readByte(registers.getHL())
                super.adc(value)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }
        currentCycle += 4
    }
}