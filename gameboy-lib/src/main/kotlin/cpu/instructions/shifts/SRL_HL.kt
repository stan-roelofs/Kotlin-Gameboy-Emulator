package gameboy.cpu.instructions.shifts

import gameboy.cpu.Registers
import gameboy.memory.Mmu
import gameboy.utils.Log

class SRL_HL(registers: Registers, mmu: Mmu) : SRL(registers, mmu) {

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
                mmu.writeByte(address, srl(value))
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}