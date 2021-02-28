package gameboy.cpu.instructions.alu

import gameboy.cpu.Registers
import gameboy.memory.Mmu

class ADD_SP_n(registers: Registers, mmu: Mmu) : ADD(registers, mmu) {

    override val totalCycles = 16

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                value = getSignedImmediate()
                add8SP(value)
            }
            8,
            12 -> {

            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}