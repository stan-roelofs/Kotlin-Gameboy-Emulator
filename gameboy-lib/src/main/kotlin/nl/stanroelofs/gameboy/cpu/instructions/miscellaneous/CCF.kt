package nl.stanroelofs.gameboy.cpu.instructions.miscellaneous

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu

class CCF(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                if (registers.getCFlag()) {
                    registers.setCFlag(false)
                } else {
                    registers.setCFlag(true)
                }

                registers.setNFlag(false)
                registers.setHFlag(false)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}