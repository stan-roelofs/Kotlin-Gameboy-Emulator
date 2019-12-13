package memory.io.sound

import memory.Mmu
import utils.getBit
import utils.setBit
import utils.toHexString

abstract class SquareWave : SoundChannel() {
    override var NR0 = 0
    override var NR1 = 0
    override var NR2 = 0
    override var NR3 = 0
    override var NR4 = 0

    protected val dutyCycles = arrayOf(0b00000001, 0b10000001, 0b10000111, 0b01111110)

    // Current duty
    protected var duty = 0

    // Current bit in duty
    protected var dutyCounter = 0

    protected var frequency = 0

    protected var timer = 0
    override val lengthCounter = LengthCounter(64)

    override fun reset() {
        super.reset()
        dutyCounter = 0
        timer = 0
    }

    override fun trigger() {
        super.trigger()
        timer = 2048 - frequency
    }

    override fun tick(cycles: Int): Int {
        lengthCounter.tick()
        if (lengthCounter.enabled && lengthCounter.length == 0) {
            enabled = false
        }

        volumeEnvelope.tick()

        timer--
        if (timer == 0) {
            timer = 2048 - frequency
            lastOutput = if (dutyCycles[duty].getBit(dutyCounter)) 1 else 0
            dutyCounter = (dutyCounter + 1) % 8
        }

        if (!enabled) {
            return 0
        }

        val volume = volumeEnvelope.volume
        return lastOutput * volume
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR11,
            Mmu.NR21 -> (this.duty shl 6) or 0b00111111 // Only bits 6-7 can be read
            Mmu.NR12,
            Mmu.NR22 -> volumeEnvelope.getNr2()
            Mmu.NR13,
            Mmu.NR23 -> 0xFF
            Mmu.NR14,
            Mmu.NR24 -> {
                var result = 0b10111111
                result = setBit(result, 6, lengthCounter.enabled)
                result
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to SquareWave")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.NR11,
            Mmu.NR21 -> {
                duty = (newVal shr 6) and 0b11
                lengthCounter.setNr1(newVal)
            }
            Mmu.NR12,
            Mmu.NR22 -> {
                volumeEnvelope.setNr2(newVal)

                if (!volumeEnvelope.getDac()) {
                    enabled = false
                }
            }
            Mmu.NR13,
            Mmu.NR23 -> {
                frequency = (frequency and 0b11100000000) or newVal
            }
            Mmu.NR14,
            Mmu.NR24 -> {
                frequency = (frequency and 0b11111111) or ((newVal and 0b111) shl 8)

                lengthCounter.enabled = newVal.getBit(6)

                if (newVal.getBit(7)) {
                    trigger()
                }
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to SquareWave")
        }
    }
}