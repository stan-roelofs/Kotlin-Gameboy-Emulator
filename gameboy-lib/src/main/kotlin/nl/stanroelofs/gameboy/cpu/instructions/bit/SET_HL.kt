package nl.stanroelofs.gameboy.cpu.instructions.bit

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.setBit

class SET_HL(registers: Registers, mmu: Mmu, private val index: Int) : Instruction(registers, mmu) {

    private var value = 0

    override val totalCycles = 16

    override fun reset() {
        super.reset()
        value = 0
    }

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
            }
            8 -> {
                value = mmu.readByte(registers.getHL())
            }
            12 -> {
                value = value.setBit(index)
                mmu.writeByte(registers.getHL(), value)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}