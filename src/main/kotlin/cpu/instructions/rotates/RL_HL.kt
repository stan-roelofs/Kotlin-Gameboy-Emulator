package cpu.instructions.rotates

import cpu.Registers
import memory.Mmu
import utils.Log

class RL_HL(registers: Registers, mmu: Mmu) : RL(registers, mmu) {

    private var address = 0
    override val totalCycles = 16

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                address = registers.getHL()
                value = mmu.readByte(address)
            }
            8 -> {
                mmu.writeByte(address, rl(value))
            }
            12 -> {

            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}