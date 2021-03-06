package nl.stanroelofs.gameboy.cpu.instructions.alu

import nl.stanroelofs.gameboy.cpu.Registers
import nl.stanroelofs.gameboy.memory.Mmu

class DEC_HL(registers: Registers, mmu: Mmu) : DEC(registers, mmu) {

    override val totalCycles = 12

    private var address = 0

    override fun reset() {
        super.reset()
        address = 0
    }

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {
                address = registers.getHL()
                value = mmu.readByte(address)
            }
            8 -> {
                mmu.writeByte(address, value - 1)

                val zFlag = mmu.readByte(address) == 0
                registers.setZFlag(zFlag)

                super.dec(value)
            }
            else -> throw IllegalStateException("Invalid cycle count: $currentCycle")
        }

        currentCycle += 4
    }
}