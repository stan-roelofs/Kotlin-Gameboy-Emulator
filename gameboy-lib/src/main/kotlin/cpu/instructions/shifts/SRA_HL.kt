package cpu.instructions.shifts

import cpu.Registers
import memory.Mmu
import utils.Log

class SRA_HL(registers: Registers, mmu: Mmu) : SRA(registers, mmu) {

    private var address = 0
    override val totalCycles = 16

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {

            }
            8 -> {
                address = registers.getHL()
                value = mmu.readByte(address)
            }
            12 -> {
                mmu.writeByte(address, sra(value))
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}