package cpu.instructions.returns

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log
import utils.setSecondByte

class RET(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var value = 0
    override val totalCycles = 16

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                value = popFromStack()
            }
            8 -> {
                value = setSecondByte(value, popFromStack())
            }
            12 -> {
                registers.PC = value
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}