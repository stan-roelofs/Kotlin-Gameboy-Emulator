package gameboy.cpu.instructions.alu

import gameboy.cpu.Registers
import gameboy.memory.Mmu

class ADC_A_n(registers: Registers, mmu: Mmu) : ADC(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {
            }
            4 -> {
                value = getImmediate()
                super.adc(value)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }
        currentCycle += 4
    }
}