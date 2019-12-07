package memory.io.sound

import memory.Mmu
import utils.getBit
import utils.toHexString

class SquareWave1 : SquareWave() {

    override val lengthCounter = LengthCounter(64)
    override var NR0 = 0
    override var NR1 = 0
    override var NR2 = 0
    override var NR3 = 0
    override var NR4 = 0

    private var timer = 0
    private var dutyCounter = 0

    init {
        reset()
    }

    override fun tick(cycles: Int): Int {
        return 0

        /*
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
        }*/
    }

    override fun trigger() {
        //timer = getFrequency()
        lengthCounter.trigger()
        volumeEnvelope.trigger()
    }

    override fun reset() {
        dacEnabled = true
        NR0 = 0x80
        NR1 = 0xBF
        NR2 = 0xF3
        NR3 = 0xFF
        NR4 = 0xBF

        timer = 0
        dutyCounter = 0
        lengthCounter.reset()
        volumeEnvelope.reset()
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

            }
            Mmu.NR11 -> {
                this.NR1 = newVal
                duty = (newVal shr 6) and 0b11
                lengthCounter.setNr1(newVal)
            }
            Mmu.NR12 -> {
                this.NR2 = newVal

                // If at least one of the upper 5 bits of NRx2 is 1, dac is enabled
                dacEnabled = (newVal and 0b11111000) != 0

                // If the DAC is off, the channel is disabled, but turning it on does not enable the channel
                lengthCounter.enabled = lengthCounter.enabled && dacEnabled

                volumeEnvelope.setNr2(newVal)
            }
            Mmu.NR13 -> {
                this.NR3 = newVal
            }
            Mmu.NR14 -> {
                this.NR4 = newVal

                if (newVal.getBit(7)) {
                    trigger()
                }
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to SquareWave2")
        }
    }
}