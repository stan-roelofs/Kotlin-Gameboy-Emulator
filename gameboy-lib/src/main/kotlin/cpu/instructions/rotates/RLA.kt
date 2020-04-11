package cpu.instructions.rotates

import cpu.Registers
import memory.Mmu
import utils.Log

class RLA(registers: Registers, mmu: Mmu) : RL(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                value = registers.A
                registers.A = rl(value)
                registers.setZFlag(false)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}