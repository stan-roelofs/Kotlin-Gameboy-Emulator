package memory.io.sound

import memory.Mmu
import utils.getBit
import utils.toHexString

class SquareWave2 : SquareWave() {

    override var NR0 = 0
    override var NR1 = 0
    override var NR2 = 0
    override var NR3 = 0
    override var NR4 = 0

    private var timer = 0
    private var counter = 0

    private var dutyCounter = 0
    override val lengthCounter = LengthCounter(64)

    init {
        reset()
    }

    override fun reset() {
        dacEnabled = false
        NR2 = 0
        NR3 = 0xFF
        NR4 = 0xBF

        timer = 0
        dutyCounter = 0
        lengthCounter.reset()
        volumeEnvelope.reset()
    }

    override fun tick(cycles: Int): Int {
        //lengthCounter.tick()
        volumeEnvelope.tick()

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

        val volume = volumeEnvelope.volume
        val temp = dutyCycles[duty].getBit(dutyCounter)

        return if (!temp) {
            0
        } else {
            volume
        }
    }

    override fun trigger() {
        timer = getFrequency()
        lengthCounter.trigger()
        volumeEnvelope.trigger()
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR21 -> (this.duty shl 6) or 0b00111111 // Only bits 6-7 can be read
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
                duty = (newVal shr 6) and 0b11
                counter = 64 - (newVal and 0b111111)

                lengthCounter.setNr1(newVal)
            }
            Mmu.NR22 -> {
                this.NR2 = newVal

                // If at least one of the upper 5 bits of NRx2 is 1, dac is enabled
                dacEnabled = (newVal and 0b11111000) != 0

                // If the DAC is off, the channel is disabled, but turning it on does not enable the channel
                lengthCounter.enabled = lengthCounter.enabled && dacEnabled

                volumeEnvelope.setNr2(newVal)
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