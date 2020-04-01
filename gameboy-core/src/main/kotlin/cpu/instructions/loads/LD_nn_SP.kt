package cpu.instructions.loads

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log
import utils.setSecondByte

class LD_nn_SP(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var address = 0
    override val totalCycles = 20

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {

            }
            8 -> {
                address = getImmediate()
            }
            12 -> {
                address = setSecondByte(address, getImmediate())
            }
            16 -> {
                val value = registers.SP
                mmu.writeWord(address, value)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}