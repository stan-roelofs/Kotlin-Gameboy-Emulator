package gameboy.cpu.instructions.bit

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.Log
import gameboy.utils.clearBit

class RES_HL(registers: Registers, mmu: Mmu, private val index: Int) : Instruction(registers, mmu) {

    override val totalCycles = 16

    private var value = 0

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
                value = clearBit(value, index)
                mmu.writeByte(registers.getHL(), value)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}