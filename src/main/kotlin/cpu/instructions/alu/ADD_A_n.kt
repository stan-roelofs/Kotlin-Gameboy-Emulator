package cpu.instructions.alu

import cpu.Registers
import memory.Mmu
import utils.Log

class ADD_A_n(registers: Registers, mmu: Mmu) : ADD(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {
                value = getImmediate()
            }
            4 -> {
                super.add8(value)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}