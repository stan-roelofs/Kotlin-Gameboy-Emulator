package nl.stanroelofs.gameboy.cpu.instructions.restarts

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.firstByte
import nl.stanroelofs.gameboy.utils.secondByte

class RST_n(registers: Registers, mmu: Mmu, private val address: Int) : Instruction(registers, mmu) {

    override val totalCycles = 16

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {

            }
            8 -> {
                pushToStack(registers.PC.secondByte)
            }
            12 -> {
                pushToStack(registers.PC.firstByte)
                registers.PC = address
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}