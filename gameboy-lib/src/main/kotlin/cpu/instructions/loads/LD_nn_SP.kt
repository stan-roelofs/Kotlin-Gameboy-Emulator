package cpu.instructions.loads

import cpu.Registers
import cpu.instructions.Instruction
import memory.Mmu
import utils.Log
import utils.getFirstByte
import utils.getSecondByte
import utils.setSecondByte

class LD_nn_SP(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var address = 0
    override val totalCycles = 20

    override fun reset() {
        super.reset()
        address = 0
    }

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
                mmu.writeByte(address, registers.SP.getFirstByte())
            }
            16 -> {
                mmu.writeByte(address + 1, registers.SP.getSecondByte())
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}