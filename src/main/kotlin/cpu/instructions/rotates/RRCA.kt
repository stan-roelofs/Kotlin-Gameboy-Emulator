package cpu.instructions.rotates

import cpu.Registers
import memory.Mmu
import utils.Log

class RRCA(registers: Registers, mmu: Mmu) : RRC(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                value = registers.A
                registers.A = rrc(value)
                registers.setZFlag(false)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}