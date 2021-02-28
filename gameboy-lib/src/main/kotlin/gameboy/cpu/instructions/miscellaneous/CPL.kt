package gameboy.cpu.instructions.miscellaneous

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu

class CPL(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    override val totalCycles = 4

    override fun tick() {
        when(currentCycle) {
            0 -> {
                var value = registers.A
                value = value.inv()
                value = value and 0xFF
                registers.A = value

                registers.setNFlag(true)
                registers.setHFlag(true)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}