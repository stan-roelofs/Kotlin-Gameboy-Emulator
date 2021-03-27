package nl.stanroelofs.gameboy.cpu.instructions.bit

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.getBit

class BIT_HL(registers: Registers, mmu: Mmu, private val index: Int) : BIT(registers, mmu) {

    override val totalCycles = 12

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {

            }
            8 -> {
                val address = registers.HL
                state = mmu.readByte(address).getBit(index)
                super.bit(state)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}