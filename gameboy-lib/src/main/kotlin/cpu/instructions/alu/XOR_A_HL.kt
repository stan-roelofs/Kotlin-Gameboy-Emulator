package cpu.instructions.alu

import cpu.Registers
import memory.Mmu
import utils.Log

class XOR_A_HL(registers: Registers, mmu: Mmu) : XOR(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {
            }
            4 -> {
                value = mmu.readByte(registers.getHL())
                xor(value)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}