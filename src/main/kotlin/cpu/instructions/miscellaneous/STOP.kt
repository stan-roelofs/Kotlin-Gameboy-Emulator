package cpu.instructions.miscellaneous

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class STOP(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                println("STOP EXECUTED")
                registers.stop = true
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}