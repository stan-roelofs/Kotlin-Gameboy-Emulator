package cpu.instructions.calls

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log
import utils.getFirstByte
import utils.getSecondByte
import utils.setSecondByte

class CALL_nn(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var address = 0
    override val totalCycles = 24

    override fun tick() {

        when(currentCycle) {
            0 -> {

            }
            4 -> {
                address = getImmediate()
            }
            8 -> {
                address = setSecondByte(address, getImmediate())
            }
            12 -> {

            }
            16 -> {
                pushToStack(registers.PC.getSecondByte())
            }
            20 -> {
                pushToStack(registers.PC.getFirstByte())
                registers.PC = address
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}