package gameboy.cpu.instructions.loads

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.getFirstByte
import gameboy.utils.getSecondByte
import gameboy.utils.setSecondByte

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
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}