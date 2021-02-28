package gameboy.cpu.instructions.rotates

import gameboy.cpu.Registers
import gameboy.memory.Mmu

class RLC_HL(registers: Registers, mmu: Mmu) : RLC(registers, mmu) {

    private var address = 0
    override val totalCycles = 16

    override fun reset() {
        super.reset()
        address = 0
    }

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {

            }
            8 -> {
                address = registers.getHL()
                value = mmu.readByte(address)
            }
            12 -> {
                mmu.writeByte(address, rlc(value))
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}