package cpu.instructions.alu

import cpu.Registers
import memory.Mmu
import utils.Log

class AND_A_HL(registers: Registers, mmu: Mmu) : AND(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {

        when(currentCycle) {
            0 -> {
                value = mmu.readByte(registers.getHL())
            }
            4 -> {
                and(value)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}