package nl.stanroelofs.gameboy.cpu.instructions.loads

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu

class LDI_HL_A(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                val HL = registers.HL
                mmu.writeByte(HL, registers.A)
                registers.HL = (HL + 1)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}