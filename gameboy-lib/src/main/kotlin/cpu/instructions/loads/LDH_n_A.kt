package cpu.instructions.loads

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log

class LDH_n_A(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var address = 0
    override val totalCycles = 12

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                address = getImmediate() + 0xFF00
            }
            8 -> {
                mmu.writeByte(address, registers.A)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}