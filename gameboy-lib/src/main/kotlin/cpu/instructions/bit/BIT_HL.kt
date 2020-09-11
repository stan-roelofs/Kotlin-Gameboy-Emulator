package gameboy.cpu.instructions.bit

import gameboy.cpu.Registers
import gameboy.memory.Mmu
import gameboy.utils.Log
import gameboy.utils.getBit

class BIT_HL(registers: Registers, mmu: Mmu, private val index: Int) : BIT(registers, mmu) {

    override val totalCycles = 12

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {

            }
            8 -> {
                val address = registers.getHL()
                state = mmu.readByte(address).getBit(index)
                super.bit(state)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}