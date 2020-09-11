package gameboy.cpu.instructions.restarts

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.Log
import gameboy.utils.getFirstByte
import gameboy.utils.getSecondByte

class RST_n(registers: Registers, mmu: Mmu, private val address: Int) : Instruction(registers, mmu) {

    override val totalCycles = 16

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {

            }
            8 -> {
                pushToStack(registers.PC.getSecondByte())
            }
            12 -> {
                pushToStack(registers.PC.getFirstByte())
                registers.PC = address
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}