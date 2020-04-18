package cpu.instructions.alu

import cpu.Registers
import memory.Mmu
import utils.Log

class SUB_A_n(registers: Registers, mmu: Mmu) : SUB(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                value = getImmediate()
                sub(value)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}