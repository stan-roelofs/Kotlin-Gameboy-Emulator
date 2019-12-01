package memory.io.sound

import memory.Mmu
import utils.getBit
import utils.toHexString

class SquareWave2 : SquareWave() {

    override var NR0 = 0
    override var NR1 = 0x3F
    override var NR2 = 0
    override var NR3 = 0xFF
    override var NR4 = 0xBF

    //private val volumeEnvelope = VolumeEnvelope()

    private var timer = 0
    private var dutyCounter = 0
    private var lengthCounter = 0
    private var volumeTimer = 0

    override fun reset() {
        NR1 = 0x3F
        NR2 = 0
        NR3 = 0xFF
        NR4 = 0xBF

        timer = 0
        dutyCounter = 0
        volumeTimer = 0
        lengthCounter = 0
    }

    override fun tick(cycles: Int): Int {
        if (!isEnabled()) {
            return 0
        }
        timer--

        if (timer == 0) {
            timer = getFrequency()
            dutyCounter++

            if (dutyCounter == 8) {
                dutyCounter = 0
            }
        }

        val volume = (NR2 shr 4) and 0b1111
        val temp = dutyCycles[duty].getBit(dutyCounter)

        return if (!temp) {
            0
        } else {
            volume
        }
    }

    private fun getFrequency(): Int {
        return 2048 - (NR3 or (NR4 and 0b111 shl 8))
    }

    override fun trigger() {
        channelEnabled = true
        timer = getFrequency()

        if (lengthCounter == 0) {
            lengthCounter = 64
        }

        volumeTimer = getFrequency()

    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR21 -> this.NR1 or 0b00111111 // Only bits 6-7 can be read
            Mmu.NR22 -> this.NR2
            Mmu.NR23 -> this.NR3
            Mmu.NR24 -> this.NR4 or 0b10111111 // Only bit 6 can be read
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to SquareWave2")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.NR21 -> {
                this.NR1 = newVal
                duty = (newVal and 0b11000000) shr 6
                lengthCounter = newVal and 0b00111111
            }
            Mmu.NR22 -> {
                this.NR2 = newVal
                dacEnabled = newVal and 0b11111000 != 0
            }
            Mmu.NR23 -> {
                this.NR3 = newVal
            }
            Mmu.NR24 -> {
                this.NR4 = newVal

                if (newVal.getBit(7)) {
                    trigger()
                }
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to SquareWave2")
        }
    }
}