package cpu.instructions.loads

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class LDI_HL_A(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                val HL = registers.getHL()
                mmu.writeByte(HL, registers.A)
                registers.setHL(HL + 1)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}