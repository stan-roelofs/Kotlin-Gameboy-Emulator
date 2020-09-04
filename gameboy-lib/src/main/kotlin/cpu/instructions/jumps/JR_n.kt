package cpu.instructions.jumps

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class JR_n(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var value = 0
    override val totalCycles = 12

    override fun reset() {
        super.reset()
        value = 0
    }

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                value = getSignedImmediate()
            }
            8 -> {
                registers.PC += value
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}