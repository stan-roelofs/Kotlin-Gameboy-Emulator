package gameboy.cpu.instructions.calls

import gameboy.cpu.Registers
import gameboy.cpu.instructions.Instruction
import gameboy.memory.Mmu
import gameboy.utils.getFirstByte
import gameboy.utils.getSecondByte
import gameboy.utils.setSecondByte

class CALL_nn(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var address = 0
    override val totalCycles = 24

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

            }
            16 -> {
                pushToStack(registers.PC.getSecondByte())
            }
            20 -> {
                pushToStack(registers.PC.getFirstByte())
                registers.PC = address
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}