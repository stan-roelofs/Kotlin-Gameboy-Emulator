package cpu.instructions.loads

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log
import utils.setSecondByte

class LD_A_nn(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var value = 0
    override val totalCycles = 16

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                value = getImmediate()
            }
            8 -> {
                value = setSecondByte(value, getImmediate())
                registers.A = mmu.readByte(value)
            }
            12 -> {

            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}