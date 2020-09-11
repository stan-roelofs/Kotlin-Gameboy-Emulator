package gameboy.cpu.instructions.loads

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.Log

class LDH_A_n(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var address = 0
    override val totalCycles = 12

    override fun reset() {
        super.reset()
        address = 0
    }

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                address = getImmediate() + 0xFF00
            }
            8 -> {
                registers.A = mmu.readByte(address)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}