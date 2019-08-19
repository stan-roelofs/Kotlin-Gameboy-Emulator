package memory.io.sound

import memory.Mmu
import utils.toHexString

class SquareWave1 : SquareWave() {

    private var NR10 = 0
    private var NR11 = 0
    private var NR12 = 0
    private var NR13 = 0
    private var NR14 = 0

    override fun reset() {
        NR10 = 0x80
        NR11 = 0xBF
        NR12 = 0xF3
        NR13 = 0xFF
        NR14 = 0xBF
    }

    override fun tick(soundBuffer: ByteArray, samplesToWrite: Int): Boolean {
        return false
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR10 -> this.NR10 or 0b10000000 // Bit 7 unused
            Mmu.NR11 -> this.NR11 or 0b00111111 // Only bits 6-7 can be read
            Mmu.NR12 -> this.NR12
            Mmu.NR13 -> this.NR13
            Mmu.NR14 -> this.NR14 or 0b10111111 // Only bit 6 can be read
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to SquareWave1")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.NR10 -> {
                this.NR10 = newVal
            }
            Mmu.NR11 -> {
                this.NR11 = newVal
            }
            Mmu.NR12 -> {
                this.NR12 = newVal
            }
            Mmu.NR13 -> {
                this.NR13 = newVal
            }
            Mmu.NR14 -> {
                this.NR14 = newVal
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to SquareWave1")
        }
    }
}