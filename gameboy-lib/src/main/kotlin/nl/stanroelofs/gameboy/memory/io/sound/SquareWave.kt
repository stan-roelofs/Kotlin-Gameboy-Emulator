package nl.stanroelofs.gameboy.memory.io.sound

import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.getBit
import nl.stanroelofs.gameboy.utils.setBit
import nl.stanroelofs.gameboy.utils.toHexString

abstract class SquareWave : SoundChannel() {
    private val dutyCycles = arrayOf(0b00000001, 0b10000001, 0b10000111, 0b01111110)

    // Current duty
    protected var duty = 0

    // Current bit in duty
    private var dutyCounter = 0

    private var off = false

    protected var timer = 0

    override fun reset() {
        super.reset()
        dutyCounter = 0
        timer = 0
        off = false
    }

    override fun powerOn() {
        super.powerOn()
        off = false
    }

    override fun powerOff() {
        reset()
        off = true

        super.powerOff()
        duty = 0
    }

    override fun trigger() {
        super.trigger()
        timer = getFrequency() * 4
    }

    override fun tick(): Int {
        volumeEnvelope.tick()
        lengthCounter.tick()

        timer--
        if (timer == 0) {
            timer = getFrequency() * 4
            lastOutput = if (dutyCycles[duty].getBit(dutyCounter)) 1 else 0
            dutyCounter = (dutyCounter + 1) % 8
        }

        if (!enabled) {
            return 0
        }

        val volume = volumeEnvelope.volume
        return if (muted) 0 else lastOutput * volume
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
                result = result.setBit(6, lengthCounter.lengthEnabled)
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
                // Only write to the length counter if the APU is off
                if (!off)
                    duty = (newVal shr 6) and 0b11

                // On DMG the length counters are not affected by power and can still be written while off
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