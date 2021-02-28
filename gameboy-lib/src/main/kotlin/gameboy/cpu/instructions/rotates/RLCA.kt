package gameboy.cpu.instructions.rotates

import gameboy.cpu.Registers
import gameboy.memory.Mmu

class RLCA(registers: Registers, mmu: Mmu) : RLC(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                value = registers.A
                registers.A = rlc(value)
                registers.setZFlag(false)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}