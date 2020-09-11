package gameboy.cpu.instructions.alu

import gameboy.cpu.Registers
import gameboy.memory.Mmu
import gameboy.utils.Log

class OR_A_n(registers: Registers, mmu: Mmu) : OR(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {
            }
            4 -> {
                value = getImmediate()
                or(value)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}