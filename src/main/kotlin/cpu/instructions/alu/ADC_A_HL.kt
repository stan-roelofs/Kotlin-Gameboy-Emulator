package cpu.instructions.alu

import memory.Mmu
import cpu.Registers

class ADC_A_HL(registers: Registers, mmu: Mmu) : ADC(registers, mmu) {

    override fun execute(): Int {
        var value = mmu.readByte(registers.getHL())

        super.adc(value)

        return 8
    }
}