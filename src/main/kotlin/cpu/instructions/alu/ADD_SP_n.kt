package cpu.instructions.alu

import cpu.Registers
import memory.Mmu
import utils.Log

class ADD_SP_n(registers: Registers, mmu: Mmu) : ADD(registers, mmu) {

    override val totalCycles = 16

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                value = getSignedImmediate()
                add8SP(value)
            }
            8,
            16 -> {

            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}