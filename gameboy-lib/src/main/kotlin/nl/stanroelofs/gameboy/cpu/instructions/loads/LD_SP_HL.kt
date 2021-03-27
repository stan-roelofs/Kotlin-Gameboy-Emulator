package nl.stanroelofs.gameboy.cpu.instructions.loads

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu

class LD_SP_HL(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                val value = registers.HL
                registers.SP = value
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}