package gameboy.cpu.instructions.miscellaneous

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.Log

class HALT(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                if (registers.IME) {
                    registers.halt = true
                } else {
                    // If IME = 0
                    val IE = mmu.readByte(Mmu.IE)
                    val IF = mmu.readByte(Mmu.IF)

                    // If (IE & IF & 0x1F) == 0 (no interrupt is pending), halt mode is entered
                    // If (IE & IF & 0x1F) != 0 (an interrupt is pending), halt bug occurs
                    if ((IE and IF and 0x1F) == 0) {
                        registers.halt = true
                    } else {
                        registers.haltBug = true
                    }
                }
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}