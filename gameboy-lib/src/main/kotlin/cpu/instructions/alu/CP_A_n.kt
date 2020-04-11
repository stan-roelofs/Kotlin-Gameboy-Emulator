package cpu.instructions.alu

import cpu.Registers
import memory.Mmu
import utils.Log

class CP_A_n(registers: Registers, mmu: Mmu) : CP(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {

        when(currentCycle) {
            0 -> {
                value = getImmediate()
            }
            4 -> {
                cp(value)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}