package memory.io.sound

import GameBoy
import memory.Memory
import memory.Mmu
import utils.getBit
import utils.setBit
import utils.toHexString
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.SourceDataLine

class Sound : Memory {

    private var NR50 = 0
    private var NR51 = 0

    private val patternRam = IntArray(0x10)

    private val square1 = SquareWave1()
    private val square2 = SquareWave2()
    private val wave = WaveChannel()
    private val noise = NoiseChannel()

    private val allChannels: Array<SoundChannel> = arrayOf(square1, square2, wave, noise)
    private val samples = IntArray(4)

    private var enabled = false

    private val SAMPLE_RATE = 48000f
    private val BUFFER_SIZE = 1024
    private val AUDIO_FORMAT = AudioFormat(SAMPLE_RATE, 8, 2, false, false)
    private val buffer = ByteArray(BUFFER_SIZE)
    private var sampleCounter = 0
    private var bufferCounter = 0
    private var rate = (GameBoy.TICKS_PER_SEC / SAMPLE_RATE).toInt()

    private var line : SourceDataLine? = null

    init {
        enabled = true

        try {
            line = AudioSystem.getSourceDataLine(AUDIO_FORMAT)
            line?.open(AUDIO_FORMAT)
            line?.start()
        } catch (e: LineUnavailableException) {
            throw RuntimeException(e)
        }
    }

    override fun reset() {
        NR50 = 0x77
        NR51 = 0xF3
        //NR52 = 0xF1

        patternRam.fill(0)
    }

    fun tick(cycles: Int) {
        if (!enabled) {
            return
        }

        for (i in 0 until 4) {
            samples[i] = allChannels[i].tick(cycles)
        }

        sampleCounter++
        if (sampleCounter == rate) {
            sampleCounter = 0

            var left = 0
            var right = 0

            for (i in 0 until 4) {
                if (NR51 and (1 shl i + 4) != 0) {
                    left += samples[i]
                }
                if (NR51 and (1 shl i) != 0) {
                    right += samples[i]
                }
            }
            left /= 4
            right /= 4

            // Bits 4..6 contain left volume
            left *= ((NR50 shl 4) and 0b111) + 1
            // Bits 0..2 contain right volume
            right *= (NR50 and 0b111) + 1

            buffer[bufferCounter] = left.toByte()
            buffer[bufferCounter + 1] = right.toByte()

            bufferCounter += 2
            if (bufferCounter == BUFFER_SIZE) {
                bufferCounter = 0
                line!!.write(buffer, 0, BUFFER_SIZE)
            }
        }
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR10,
            Mmu.NR11,
            Mmu.NR12,
            Mmu.NR13,
            Mmu.NR14 -> {
                square1.readByte(address)
            }
            Mmu.NR21,
            Mmu.NR22,
            Mmu.NR23,
            Mmu.NR24 -> {
                square2.readByte(address)
            }
            Mmu.NR30,
            Mmu.NR31,
            Mmu.NR32,
            Mmu.NR33,
            Mmu.NR34 -> {
                wave.readByte(address)
            }
            Mmu.NR41,
            Mmu.NR42,
            Mmu.NR43,
            Mmu.NR44 -> {
                noise.readByte(address)
            }
            Mmu.NR50 -> this.NR50
            Mmu.NR51 -> this.NR51
            Mmu.NR52 -> {
                // Bits 0-3 are statuses of channels (1, 2, wave, noise)
                var result = 0b01110000 // Bits 4-6 are unused
                if (square1.isEnabled()) {
                    result = setBit(result, 0)
                }
                if (square2.isEnabled()) {
                    result = setBit(result, 1)
                }
                if (wave.isEnabled()) {
                    result = setBit(result, 2)
                }
                if (noise.isEnabled()) {
                    result = setBit(result, 3)
                }

                // Bit 7 is sound status
                if (enabled) {
                    result = setBit(result, 7)
                }
                result
            }
            in 0xFF30..0xFF3F -> patternRam[address - 0xFF30]
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Sound")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.NR10,
            Mmu.NR11,
            Mmu.NR12,
            Mmu.NR13,
            Mmu.NR14 -> {
                square1.writeByte(address,  value)
            }
            Mmu.NR21,
            Mmu.NR22,
            Mmu.NR23,
            Mmu.NR24 -> {
                square2.writeByte(address,  value)
            }
            Mmu.NR30,
            Mmu.NR31,
            Mmu.NR32,
            Mmu.NR33,
            Mmu.NR34 -> {
                wave.writeByte(address,  value)
            }
            Mmu.NR41,
            Mmu.NR42,
            Mmu.NR43,
            Mmu.NR44 -> {
                noise.writeByte(address,  value)
            }
            Mmu.NR50 -> this.NR50 = newVal
            Mmu.NR51 -> this.NR51 = newVal
            Mmu.NR52 -> {
                enabled = value.getBit(7)
            }
            in 0xFF30..0xFF3F -> patternRam[address - 0xFF30] = newVal
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Sound")
        }
    }

}