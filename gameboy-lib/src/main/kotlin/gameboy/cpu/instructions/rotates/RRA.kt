package gameboy.cpu.instructions.rotates

import gameboy.cpu.Registers
import gameboy.memory.Mmu

class RRA(registers: Registers, mmu: Mmu) : RR(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                value = registers.A
                registers.A = rr(value)
                registers.setZFlag(false)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}