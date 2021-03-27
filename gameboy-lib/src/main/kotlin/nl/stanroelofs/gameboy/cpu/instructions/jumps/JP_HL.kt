package nl.stanroelofs.gameboy.cpu.instructions.jumps

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu

class JP_HL(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                val value = registers.HL
                registers.PC = value
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}