package gameboy.cpu.instructions.loads

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu

class LD_HL_n(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var value = 0
    override val totalCycles = 12

    override fun reset() {
        super.reset()
        value = 0
    }

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                value = getImmediate()
            }
            8 -> {
                mmu.writeByte(registers.getHL(), value)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}