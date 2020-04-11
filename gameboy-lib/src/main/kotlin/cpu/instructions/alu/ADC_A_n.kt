package cpu.instructions.alu

import cpu.Registers
import memory.Mmu
import utils.Log

class ADC_A_n(registers: Registers, mmu: Mmu) : ADC(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {
                value = getImmediate()
            }
            4 -> {
                super.adc(value)
            }
            else -> Log.e("Invalid state")
        }
        currentCycle += 4
    }
}