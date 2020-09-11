package gameboy.cpu.instructions.loads

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.Log

class LDH_C_A(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {
        when(currentCycle) {
            0 -> {
            }
            4 -> {
                mmu.writeByte(registers.C + 0xFF00, registers.A)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}