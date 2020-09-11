package gameboy.cpu.instructions.loads

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.Log

class LDD_A_HL(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 8

    override fun tick() {

        when(currentCycle) {
            0 -> {

            }
            4 -> {
                val HL = registers.getHL()
                val value = mmu.readByte(HL)
                registers.setHL(HL - 1)

                registers.A = value
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}