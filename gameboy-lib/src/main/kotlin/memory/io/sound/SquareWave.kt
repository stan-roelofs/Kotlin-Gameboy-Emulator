package memory.io.sound

import memory.Mmu
import utils.getBit
import utils.setBit
import utils.toHexString

abstract class SquareWave : SoundChannel() {
    private val dutyCycles = arrayOf(0b00000001, 0b10000001, 0b10000111, 0b01111110)

    // Current duty
    protected var duty = 0

    // Current bit in duty
    private var dutyCounter = 0

    protected var timer = 0
    override val lengthCounter = LengthCounter(64)

    override fun reset() {
        super.reset()
        dutyCounter = 0
        timer = 0
    }

    override fun powerOff() {
        reset()

        super.powerOff()
        duty = 0
    }

    override fun trigger() {
        super.trigger()
        timer = getFrequency()
    }

    override fun tick(cycles: Int): Int {
        volumeEnvelope.tick()

        lengthCounter.tick()
        if (lengthCounter.enabled && lengthCounter.length == 0) {
            enabled = false
        }

        timer--
        if (timer == 0) {
            timer = getFrequency()
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
                lengthCounter.setNr1(newVal and 0b00111111)
            }
            Mmu.NR12,
            Mmu.NR22 -> {
                volumeEnvelope.setNr2(newVal)

                if (!volumeEnvelope.getDac()) {
                    enabled = false
                }
            }
            Mmu.NR14,
            Mmu.NR24 -> {
                lengthCounter.setNr4(newVal)

                if (newVal.getBit(7)) {
                    trigger()
                }
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to SquareWave")
        }
    }

    abstract fun getFrequency(): Int
}