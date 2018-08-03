package cpu.instructions.alu

import Mmu
import cpu.Registers

class ADC_A_n(registers: Registers, mmu: Mmu) : ADC(registers, mmu) {

    override fun execute(): Int {
        val value = getImmediate()

        super.adc(value)

        return 8
    }
}