package gameboy.cpu.instructions.alu

import gameboy.cpu.Registers
import gameboy.memory.Mmu

class AND_A_HL(registers: Registers, mmu: Mmu) : AND(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {

        when(currentCycle) {
            0 -> {
            }
            4 -> {
                value = mmu.readByte(registers.getHL())
                and(value)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}