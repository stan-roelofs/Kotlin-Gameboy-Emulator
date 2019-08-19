package memory.io.sound

import memory.Mmu
import utils.toHexString
import java.io.Serializable

class NoiseChannel : SoundChannel, Serializable {

    private var NR41 = 0
    private var NR42 = 0
    private var NR43 = 0
    private var NR44 = 0

    override fun reset() {
        NR41 = 0xFF
        NR42 = 0x00
        NR43 = 0x00
        NR44 = 0xBF
    }

    override fun tick(soundBuffer: ByteArray, samples: Int): Boolean {
        return false
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR41 -> this.NR41 or 0b11000000 // Bits 6-7 unused
            Mmu.NR42 -> this.NR42
            Mmu.NR43 -> this.NR43
            Mmu.NR44 -> this.NR44 or 0b10111111 // Only bit 6 can be read
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to NoiseChannel")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.NR41 -> {
                this.NR41 = newVal
            }
            Mmu.NR42 -> {
                this.NR42 = newVal
            }
            Mmu.NR43 -> {
                this.NR43 = newVal
            }
            Mmu.NR44 -> {
                this.NR44 = newVal
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to NoiseChannel")
        }
    }
}