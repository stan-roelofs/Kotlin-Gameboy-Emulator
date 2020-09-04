package cpu.instructions.bit

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log
import utils.setBit

class SET_HL(registers: Registers, mmu: Mmu, private val index: Int) : Instruction(registers, mmu) {

    private var value = 0

    override val totalCycles = 16

    override fun reset() {
        super.reset()
        value = 0
    }

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
            }
            8 -> {
                value = mmu.readByte(registers.getHL())
            }
            12 -> {
                value = setBit(value, index)
                mmu.writeByte(registers.getHL(), value)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}