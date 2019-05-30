package memory.io.sound

import memory.Memory
import memory.Mmu
import utils.toHexString
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.SourceDataLine


class Sound : Memory {

    companion object {
        val SAMPLE_RATE = 131072 / 3
    }

    private var NR10 = 0
    private var NR11 = 0
    private var NR12 = 0
    private var NR13 = 0
    private var NR14 = 0

    private var NR21 = 0
    private var NR22 = 0
    private var NR23 = 0
    private var NR24 = 0

    private var NR30 = 0
    private var NR31 = 0
    private var NR32 = 0
    private var NR33 = 0
    private var NR34 = 0

    private var NR41 = 0
    private var NR42 = 0
    private var NR43 = 0
    private var NR44 = 0

    private var NR50 = 0
    private var NR51 = 0
    private var NR52 = 0

    private val patternRam = IntArray(0x10)

    val SQUARE1 = 0
    val SQUARE2 = 1
    val WAVE = 2
    val NOISE = 3

    var leftEnabled = Array<Boolean>(4){true}
    var rightEnabled = Array<Boolean>(4){true}

    private val square1 = SquareWave()
    private val square2 = SquareWave()
    private val wave = WaveChannel()
    private val noise = NoiseChannel()

    private lateinit var sourceDL : SourceDataLine
    val SAMPLES_PER_FRAME = SAMPLE_RATE / 57
    val AUDIO_FORMAT = AudioFormat(SAMPLE_RATE.toFloat(), 8, 2, false, false)

    private val masterBuffer = ByteArray(SAMPLES_PER_FRAME * 2) {0}
    private val tempBuffer = ByteArray(SAMPLES_PER_FRAME) {0}


    init {
        try {
            sourceDL = AudioSystem.getSourceDataLine(AUDIO_FORMAT)
            sourceDL.open(AUDIO_FORMAT)
            sourceDL.start()
        } catch (e: LineUnavailableException) {
            e.printStackTrace()
        }
    }

    override fun reset() {
        NR10 = 0x80
        NR11 = 0xBF
        NR12 = 0xF3
        NR13 = 0xFF
        NR14 = 0xBF

        NR21 = 0x3F
        NR22 = 0
        NR23 = 0xFF
        NR24 = 0xBF

        NR30 = 0x7F
        NR31 = 0xFF
        NR32 = 0x9F
        NR33 = 0xFF
        NR34 = 0xBF

        NR41 = 0xFF
        NR42 = 0x00
        NR43 = 0x00
        NR44 = 0xBF

        NR50 = 0x77
        NR51 = 0xF3
        NR52 = 0xF1

        patternRam.fill(0)
    }

    fun frame() {

        val samplesToWrite = Math.min(sourceDL.available() / 2, SAMPLES_PER_FRAME)
        masterBuffer.fill(0)

        var channelEnabled = square1.tick(tempBuffer, samplesToWrite)
        if (channelEnabled) {
            if (leftEnabled[SQUARE1]) {
                for (i in 0 until samplesToWrite) {
                    masterBuffer[2 * i] = (masterBuffer[2 * i] + tempBuffer[i]).toByte()
                }
            }
            if (rightEnabled[SQUARE1]) {
                for (i in 0 until samplesToWrite) {
                    masterBuffer[2 * i + 1] = (masterBuffer[2 * i + 1] + tempBuffer[i]).toByte()
                }
            }
        }

        channelEnabled = square2.tick(tempBuffer, samplesToWrite)
        if (channelEnabled) {
            if (leftEnabled[SQUARE2]) {
                for (i in 0 until samplesToWrite) {
                    masterBuffer[2 * i] = (masterBuffer[2 * i] + tempBuffer[i]).toByte()
                }
            }
            if (rightEnabled[SQUARE2]) {
                for (i in 0 until samplesToWrite) {
                    masterBuffer[2 * i + 1] = (masterBuffer[2 * i + 1] + tempBuffer[i]).toByte()
                }
            }
        }

        channelEnabled = wave.tick(tempBuffer, samplesToWrite)
        if (channelEnabled) {
            if (leftEnabled[WAVE]) {
                for (i in 0 until samplesToWrite) {
                    masterBuffer[2 * i] = (masterBuffer[2 * i] + tempBuffer[i]).toByte()
                }
            }
            if (rightEnabled[WAVE]) {
                for (i in 0 until samplesToWrite) {
                    masterBuffer[2 * i + 1] = (masterBuffer[2 * i + 1] + tempBuffer[i]).toByte()
                }
            }
        }

        channelEnabled = noise.tick(tempBuffer, samplesToWrite)
        if (channelEnabled) {
            if (leftEnabled[NOISE]) {
                for (i in 0 until samplesToWrite) {
                    masterBuffer[2 * i] = (masterBuffer[2 * i] + tempBuffer[i]).toByte()
                }
            }
            if (rightEnabled[NOISE]) {
                for (i in 0 until samplesToWrite) {
                    masterBuffer[2 * i + 1] = (masterBuffer[2 * i + 1] + tempBuffer[i]).toByte()
                }
            }
        }

        sourceDL.write(masterBuffer, 0, samplesToWrite * 2)
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR10 -> this.NR10 or 0b10000000 // Bit 7 unused
            Mmu.NR11 -> this.NR11 or 0b00111111 // Only bits 6-7 can be read
            Mmu.NR12 -> this.NR12
            Mmu.NR13 -> this.NR13
            Mmu.NR14 -> this.NR14 or 0b10111111 // Only bit 6 can be read
            Mmu.NR21 -> this.NR21 or 0b00111111 // Only bits 6-7 can be read
            Mmu.NR22 -> this.NR22
            Mmu.NR23 -> this.NR23
            Mmu.NR24 -> this.NR24 or 0b10111111 // Only bit 6 can be read
            Mmu.NR30 -> this.NR30 or 0b01111111 // Only bit 7 can be read
            Mmu.NR31 -> this.NR31
            Mmu.NR32 -> this.NR32 or 0b10011111 // Only bits 5-6 can be read
            Mmu.NR33 -> this.NR33
            Mmu.NR34 -> this.NR34 or 0b10111111 // Only bit 6 can be read
            Mmu.NR41 -> this.NR41 or 0b11000000 // Bits 6-7 unused
            Mmu.NR42 -> this.NR42
            Mmu.NR43 -> this.NR43
            Mmu.NR44 -> this.NR44 or 0b10111111 // Only bit 6 can be read
            Mmu.NR50 -> this.NR50
            Mmu.NR51 -> this.NR51
            Mmu.NR52 -> this.NR52 or 0b01110000 // Bits 4-6 unused
            in 0xFF30..0xFF3F -> patternRam[address - 0xFF30]
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Sound")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            in 0xFF10..0xFF14 -> {
                square1.handleByte(address - 0xFF10,  value)
            }
            in 0xFF15..0xFF19 -> {
                square2.handleByte(address - 0xFF10,  value)
            }
            in 0xFF1A..0xFF1E -> {
                wave.handleByte(address - 0xFF10,  value)
            }
            in 0xFF1F..0xFF23 -> {
                noise.handleByte(address - 0xFF10,  value)
            }
            Mmu.NR10 -> this.NR10 = newVal
            Mmu.NR11 -> this.NR11 = newVal
            Mmu.NR12 -> this.NR12 = newVal
            Mmu.NR13 -> this.NR13 = newVal
            Mmu.NR14 -> this.NR14 = newVal
            Mmu.NR21 -> this.NR21 = newVal
            Mmu.NR22 -> this.NR22 = newVal
            Mmu.NR23 -> this.NR23 = newVal
            Mmu.NR24 -> this.NR24 = newVal
            Mmu.NR30 -> this.NR30 = newVal
            Mmu.NR31 -> this.NR31 = newVal
            Mmu.NR32 -> this.NR32 = newVal
            Mmu.NR33 -> this.NR33 = newVal
            Mmu.NR34 -> this.NR34 = newVal
            Mmu.NR41 -> this.NR41 = newVal
            Mmu.NR42 -> this.NR42 = newVal
            Mmu.NR43 -> this.NR43 = newVal
            Mmu.NR44 -> this.NR44 = newVal
            Mmu.NR50 -> this.NR50 = newVal
            Mmu.NR51 -> this.NR51 = newVal
            Mmu.NR52 -> this.NR52 = newVal
            in 0xFF30..0xFF3F -> patternRam[address - 0xFF30] = newVal
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Sound")
        }
    }

}