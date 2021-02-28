package gameboy.cpu.instructions.bit

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.setBit

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
                value = setBit(value, index)
                mmu.writeByte(registers.getHL(), value)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}