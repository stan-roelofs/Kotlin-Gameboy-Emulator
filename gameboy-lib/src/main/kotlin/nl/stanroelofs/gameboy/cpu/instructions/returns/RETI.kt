package nl.stanroelofs.gameboy.cpu.instructions.returns

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.setSecondByte

class RETI(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var value = 0
    override val totalCycles = 16

    override fun reset() {
        super.reset()
        value = 0
    }

    override fun tick() {
        when(currentCycle) {
            0 -> {
                registers.IME = true
            }
            4 -> {
                value = popFromStack()
            }
            8 -> {
                value = value.setSecondByte(popFromStack())
            }
            12 -> {
                registers.PC = value
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}