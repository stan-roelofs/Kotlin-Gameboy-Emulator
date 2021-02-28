package gameboy.cpu.instructions.miscellaneous

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu

class DAA(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                if (!registers.getNFlag()) {
                    if (registers.getCFlag() || registers.A > 0x99) {
                        registers.A += 0x60
                        registers.setCFlag(true)
                    }
                    if (registers.getHFlag() || (registers.A and 0x0F) > 0x09) {
                        registers.A += 0x6
                    }
                } else {
                    if (registers.getCFlag()) {
                        registers.A -= 0x60
                    }
                    if (registers.getHFlag()) {
                        registers.A -= 0x6
                    }
                }

                val zFlag = registers.A == 0
                registers.setZFlag(zFlag)

                registers.setHFlag(false)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}