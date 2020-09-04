package cpu.instructions.miscellaneous

import cpu.Registers
import memory.Mmu
import utils.Log

class SWAP_HL(registers: Registers, mmu: Mmu) : SWAP(registers, mmu) {

    private var new = 0
    private var address = 0
    override val totalCycles = 16

    override fun reset() {
        super.reset()
        new = 0
        address = 0
    }

    override fun tick() {
        when(currentCycle) {
            0 -> {

            }
            4 -> {

            }
            8 -> {
                address = registers.getHL()
                val value = mmu.readByte(address)
                new = swap(value)
            }
            12 -> {
                mmu.writeByte(address, new)

                val zFlag = new == 0

                registers.setZFlag(zFlag)
                registers.setNFlag(false)
                registers.setHFlag(false)
                registers.setCFlag(false)
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}