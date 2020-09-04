package cpu.instructions.rotates
import cpu.Registers
import memory.Mmu
import utils.Log

class RRC_HL(registers: Registers, mmu: Mmu) : RRC(registers, mmu) {

    private var address = 0
    override val totalCycles = 16

    override fun reset() {
        super.reset()
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
                value = mmu.readByte(address)
            }
            12 -> {
                mmu.writeByte(address, rrc(value))
            }
            else -> Log.e("Invalid state")
        }

        currentCycle += 4
    }
}