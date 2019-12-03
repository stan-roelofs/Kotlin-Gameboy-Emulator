package memory.io.sound

import memory.Mmu
import utils.toHexString

class WaveChannel : SoundChannel() {

    override var NR0 = 0
    override var NR1 = 0
    override var NR2 = 0
    override var NR3 = 0
    override var NR4 = 0

    init {
        reset()
    }

    override fun reset() {
        channelEnabled = false
        dacEnabled = false
        NR0 = 0x7F
        NR1 = 0xFF
        NR2 = 0x9F
        NR3 = 0xFF
        NR4 = 0xBF
    }

    override fun tick(cycles: Int): Int {
        return 0
    }

    override fun trigger() {

    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR30 -> this.NR0 or 0b01111111 // Only bit 7 can be read
            Mmu.NR31 -> this.NR1
            Mmu.NR32 -> this.NR2 or 0b10011111 // Only bits 5-6 can be read
            Mmu.NR33 -> this.NR3
            Mmu.NR34 -> this.NR4 or 0b10111111 // Only bit 6 can be read
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to WaveChannel")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.NR30 -> {
                this.NR0 = newVal
            }
            Mmu.NR31 -> {
                this.NR1 = newVal
            }
            Mmu.NR32 -> {
                this.NR2 = newVal
            }
            Mmu.NR33 -> {
                this.NR3 = newVal
            }
            Mmu.NR34 -> {
                this.NR4 = newVal
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to WaveChannel")
        }
    }
}