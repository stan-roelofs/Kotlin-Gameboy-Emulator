package gameboy.cpu.instructions.loads

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.setSecondByte

class LD_nn_A(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var address = 0
    override val totalCycles = 16

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
                mmu.writeByte(address, registers.A)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}