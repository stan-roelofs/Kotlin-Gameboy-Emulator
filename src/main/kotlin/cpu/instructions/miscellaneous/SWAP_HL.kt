package cpu.instructions.miscellaneous

import Mmu
import cpu.Registers

class SWAP_HL(registers: Registers, mmu: Mmu) : SWAP(registers, mmu) {
    override fun execute(): Int {

        val address = registers.getHL()
        val value = mmu.readByte(address)
        val new = swap(value)

        mmu.writeByte(address, new)

        val zFlag = new == 0

        registers.setZFlag(zFlag)
        registers.setNFlag(false)
        registers.setHFlag(false)
        registers.setCFlag(false)

        return 16
    }
}