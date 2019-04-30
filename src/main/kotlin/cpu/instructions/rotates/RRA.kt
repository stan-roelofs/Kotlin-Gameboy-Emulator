package cpu.instructions.rotates

import cpu.Registers
import memory.Mmu
import utils.Log

class RRA(registers: Registers, mmu: Mmu) : RR(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                value = registers.A
                registers.A = rr(value)
                registers.setZFlag(false)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}