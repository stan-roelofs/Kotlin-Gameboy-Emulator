package gameboy.cpu.instructions.rotates

import gameboy.cpu.Registers
import gameboy.memory.Mmu
import gameboy.utils.Log

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