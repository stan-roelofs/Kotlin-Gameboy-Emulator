package gameboy.cpu.instructions.jumps

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.Log
import gameboy.utils.setSecondByte

class JP_nn(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

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
                value = getImmediate()
            }
            8 -> {
                value = setSecondByte(value, getImmediate())
            }
            12 -> {
                registers.PC = value
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}