package memory.io.sound

import memory.Mmu
import utils.toHexString

class NoiseChannel : SoundChannel() {

    override val lengthCounter = LengthCounter(64)
    override var NR0 = 0
    override var NR1 = 0
    override var NR2 = 0
    override var NR3 = 0
    override var NR4 = 0

    init {
        reset()
    }

    override fun trigger() {

    }

    override fun tick(cycles: Int): Int {
        return 0
    }

    override fun reset() {
        dacEnabled = false
        NR0 = 0
        NR1 = 0xFF
        NR2 = 0x00
        NR3 = 0x00
        NR4 = 0xBF
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR41 -> this.NR1 or 0b11000000 // Bits 6-7 unused
            Mmu.NR42 -> this.NR2
            Mmu.NR43 -> this.NR3
            Mmu.NR44 -> this.NR4 or 0b10111111 // Only bit 6 can be read
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to NoiseChannel")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.NR41 -> {
                this.NR1 = newVal
            }
            Mmu.NR42 -> {
                this.NR2 = newVal
            }
            Mmu.NR43 -> {
                this.NR3 = newVal
            }
            Mmu.NR44 -> {
                this.NR4 = newVal
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to NoiseChannel")
        }
    }
}