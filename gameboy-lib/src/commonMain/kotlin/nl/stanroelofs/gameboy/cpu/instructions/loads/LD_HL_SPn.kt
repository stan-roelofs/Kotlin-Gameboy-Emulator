package nl.stanroelofs.gameboy.cpu.instructions.loads

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.cpu.instructions.Instruction
import nl.stanroelofs.gameboy.memory.Mmu

class LD_HL_SPn(registers: Registers, mmu: Mmu) : Instruction(registers, mmu) {

    private var value = 0
    override val totalCycles = 12

    override fun reset() {
        super.reset()
        value = 0
    }

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                value = getSignedImmediate()
            }
            8 -> {
                registers.HL = (registers.SP + value)

                registers.setZFlag(false)
                registers.setNFlag(false)

                val cFlag = ((registers.SP and 0xFF) + (value and 0xFF)) > 0xFF
                registers.setCFlag(cFlag)

                val hFlag = (registers.SP and 0xF) + (value and 0xF) > 0xF
                registers.setHFlag(hFlag)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}