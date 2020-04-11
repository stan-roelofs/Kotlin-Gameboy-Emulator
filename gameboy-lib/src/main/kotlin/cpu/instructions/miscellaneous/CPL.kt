package cpu.instructions.miscellaneous

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class CPL(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                var value = registers.A
                value = value.inv()
                value = value and 0xFF
                registers.A = value

                registers.setNFlag(true)
                registers.setHFlag(true)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}