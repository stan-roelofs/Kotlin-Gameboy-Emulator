package gameboy.cpu.instructions.rotates

import gameboy.cpu.Registers
import gameboy.memory.Mmu

class RLA(registers: Registers, mmu: Mmu) : RL(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                value = registers.A
                registers.A = rl(value)
                registers.setZFlag(false)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}