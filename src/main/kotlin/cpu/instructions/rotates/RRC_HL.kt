package cpu.instructions.rotates
import Mmu
import cpu.Registers

class RRC_HL(registers: Registers, mmu: Mmu) : RRC(registers, mmu) {

    override fun execute(): Int {
        val address = registers.getHL()
        val value = mmu.readByte(address)
        mmu.writeByte(address, rrc(value))

        return 16
    }
}