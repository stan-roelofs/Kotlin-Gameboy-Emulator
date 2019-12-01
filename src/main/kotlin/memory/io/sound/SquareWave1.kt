package memory.io.sound

import memory.Mmu
import utils.toHexString

class SquareWave1 : SquareWave() {

    override var NR0 = 0x80
    override var NR1 = 0xBF
    override var NR2 = 0xF3
    override var NR3 = 0xFF
    override var NR4 = 0xBF

    init {
        channelEnabled = true
        dacEnabled = true
    }

    override fun tick(cycles: Int): Int {
        return 0
    }

    override fun trigger() {

    }

    override fun reset() {
        NR1 = 0x80
        NR1 = 0xBF
        NR1 = 0xF3
        NR1 = 0xFF
        NR1 = 0xBF
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR10 -> this.NR0 or 0b10000000 // Bit 7 unused
            Mmu.NR11 -> this.NR1 or 0b00111111 // Only bits 6-7 can be read
            Mmu.NR12 -> this.NR2
            Mmu.NR13 -> this.NR3
            Mmu.NR14 -> this.NR4 or 0b10111111 // Only bit 6 can be read
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to SquareWave1")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.NR10 -> {
                this.NR0 = newVal
            }
            Mmu.NR11 -> {
                this.NR1 = newVal
            }
            Mmu.NR12 -> {
                this.NR2 = newVal
            }
            Mmu.NR13 -> {
                this.NR3 = newVal
            }
            Mmu.NR14 -> {
                this.NR4 = newVal
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to SquareWave1")
        }
    }
}