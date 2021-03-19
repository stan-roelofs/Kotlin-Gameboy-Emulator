package nl.stanroelofs.gameboy.cpu.instructions.loads

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.firstByte
import nl.stanroelofs.gameboy.utils.secondByte
import nl.stanroelofs.gameboy.utils.setSecondByte

class LD_nn_SP(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var address = 0
    override val totalCycles = 20

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
                address = address.setSecondByte(getImmediate())
            }
            12 -> {
                mmu.writeByte(address, registers.SP.firstByte)
            }
            16 -> {
                mmu.writeByte(address + 1, registers.SP.secondByte)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}