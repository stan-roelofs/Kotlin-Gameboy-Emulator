package cpu.instructions.miscellaneous

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class CCF(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                if (registers.getCFlag()) {
                    registers.setCFlag(false)
                } else {
                    registers.setCFlag(true)
                }

                registers.setNFlag(false)
                registers.setHFlag(false)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}