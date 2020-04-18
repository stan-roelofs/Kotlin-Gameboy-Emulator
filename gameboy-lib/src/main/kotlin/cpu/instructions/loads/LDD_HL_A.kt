package cpu.instructions.loads

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class LDD_HL_A(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                val HL = registers.getHL()
                registers.setHL(HL - 1)

                mmu.writeByte(HL, registers.A)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}