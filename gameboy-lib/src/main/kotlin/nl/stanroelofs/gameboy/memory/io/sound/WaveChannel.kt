package nl.stanroelofs.gameboy.memory.io.sound

import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.getBit
import nl.stanroelofs.gameboy.utils.setBit
import nl.stanroelofs.gameboy.utils.toHexString

class WaveChannel : SoundChannel() {

    private var volumeShift = 4
    private var volumeCode = 0
    private var dac = false
    private val patternRam = IntArray(0x10)
    private var frequency = 0
    private var timer = 0
    private var positionCounter = 0

    override val lengthCounter = LengthCounter(256, this)

    init {
        reset()
    }

    override fun reset() {
        dac = false
        volumeShift = 4
        volumeCode = 0
        frequency = 0
        timer = 0
        positionCounter = 0
        patternRam.fill(0)
    }

    override fun powerOff() {
        super.powerOff()
        dac = false
        volumeShift = 4
        volumeCode = 0
        frequency = 0
        timer = 0
        positionCounter = 0
    }

    override fun tick(): Int {
        lengthCounter.tick()

        if (!enabled)
            return 0

        timer--
        if (timer == 0) {
            // Reset timer
            timer = (2048 - frequency) / 2 * 4

            // Set sample buffer
            lastOutput = patternRam[positionCounter / 2]
            if (positionCounter % 2 == 0) {
                lastOutput = lastOutput shr 4
            }
            lastOutput = (lastOutput and 0x0f) shr volumeShift

            // Increase sample position counter
            positionCounter = (positionCounter + 1) % 32
        }

        return if (muted) 0 else lastOutput
    }

    override fun trigger() {
        enabled = true
        timer = (2048 - frequency) / 2 * 4
        positionCounter = 0

        if (!dac) {
            enabled = false
        }
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR30 -> {
                var result = 0b01111111
                result = result.setBit(7, dac)
                result
            }
            Mmu.NR31 -> 0b11111111
            Mmu.NR32 -> 0b10011111 or (volumeCode shl 5)
            Mmu.NR33 -> 0b11111111
            Mmu.NR34 -> {
                var result = 0b10111111
                result = result.setBit(6, lengthCounter.lengthEnabled)
                result
            }
            in 0xFF30..0xFF3F -> this.patternRam[address - 0xFF30]
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to WaveChannel")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.NR30 -> {
                dac = newVal.getBit(7)
                enabled = enabled && dac
            }
            Mmu.NR31 -> lengthCounter.setNr1(newVal)
            Mmu.NR32 -> {
                volumeCode = (newVal and 0b01100000) shr 5
                volumeShift = when(volumeCode) {
                    0 -> 4
                    1 -> 0
                    2 -> 1
                    3 -> 2
                    else -> throw IllegalStateException("Invalid volume code")
                }
            }
            Mmu.NR33 -> frequency = (frequency and 0b11100000000) or newVal
            Mmu.NR34 -> {
                frequency = (frequency and 0b11111111) or ((newVal and 0b111) shl 8)

                lengthCounter.setNr4(newVal)

                if (newVal.getBit(7)) {
                    trigger()
                }
            }
            in 0xFF30..0xFF3F -> this.patternRam[address - 0xFF30] = newVal
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to WaveChannel")
        }
    }
}