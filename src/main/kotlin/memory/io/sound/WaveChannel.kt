package memory.io.sound

import memory.Mmu
import utils.toHexString
import java.io.Serializable

class WaveChannel : SoundChannel, Serializable {

    private var NR30 = 0
    private var NR31 = 0
    private var NR32 = 0
    private var NR33 = 0
    private var NR34 = 0

    override fun reset() {
        NR30 = 0x7F
        NR31 = 0xFF
        NR32 = 0x9F
        NR33 = 0xFF
        NR34 = 0xBF
    }

    override fun tick(soundBuffer: ByteArray, samples: Int): Boolean {
        return false
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR30 -> this.NR30 or 0b01111111 // Only bit 7 can be read
            Mmu.NR31 -> this.NR31
            Mmu.NR32 -> this.NR32 or 0b10011111 // Only bits 5-6 can be read
            Mmu.NR33 -> this.NR33
            Mmu.NR34 -> this.NR34 or 0b10111111 // Only bit 6 can be read
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to WaveChannel")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.NR30 -> {
                this.NR30 = newVal
            }
            Mmu.NR31 -> {
                this.NR31 = newVal
            }
            Mmu.NR32 -> {
                this.NR32 = newVal
            }
            Mmu.NR33 -> {
                this.NR33 = newVal
            }
            Mmu.NR34 -> {
                this.NR34 = newVal
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to WaveChannel")
        }
    }
}