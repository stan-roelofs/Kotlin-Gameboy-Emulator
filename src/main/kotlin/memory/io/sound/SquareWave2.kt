package memory.io.sound

import memory.Mmu
import utils.setFirstByte
import utils.setSecondByte
import utils.toHexString

class SquareWave2 : SquareWave() {

    private var NR21 = 0
    private var NR22 = 0
    private var NR23 = 0
    private var NR24 = 0

    private var timer = 0
    private var frequency = 0
    private var duty = 0
    private var dutyTimer = 0
    private var volume = 0

    override fun reset() {
        NR21 = 0x3F
        NR22 = 0
        NR23 = 0xFF
        NR24 = 0xBF

        duty = 0
        dutyTimer = 0
        frequency = 0x7FF
        timer = (2048 - frequency) * 4
    }

    override fun tick(soundBuffer: ByteArray, samplesToWrite: Int): Boolean {
        timer--

        if (timer == 0) {
            timer = (2048 - frequency) * 4

            dutyTimer = (dutyTimer + 1) % 8
        }

        return true
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR21 -> this.NR21 or 0b00111111 // Only bits 6-7 can be read
            Mmu.NR22 -> this.NR22
            Mmu.NR23 -> this.NR23
            Mmu.NR24 -> this.NR24 or 0b10111111 // Only bit 6 can be read
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to SquareWave2")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.NR21 -> {
                this.NR21 = newVal
                this.duty = (this.NR21 shr 6) and 0b11
            }
            Mmu.NR22 -> {
                this.NR22 = newVal
            }
            Mmu.NR23 -> {
                this.NR23 = newVal
                this.frequency = setFirstByte(this.frequency, this.NR23)
            }
            Mmu.NR24 -> {
                this.NR24 = newVal
                this.frequency = setSecondByte(this.frequency, this.NR24 and 0b111)
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to SquareWave2")
        }
    }
}