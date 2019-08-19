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

    private val square1 = SquareWave1()
    private val square2 = SquareWave2()
    private val wave = WaveChannel()
    private val noise = NoiseChannel()

    private lateinit var sourceDL : SourceDataLine
    val SAMPLES_PER_FRAME = SAMPLE_RATE / 60
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
        NR50 = 0x77
        NR51 = 0xF3
        NR52 = 0xF1

        patternRam.fill(0)
    }

    fun frame() {
        /*
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
        */
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
            Mmu.NR52 -> this.NR52 or 0b01110000 // Bits 4-6 unused
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
            Mmu.NR52 -> this.NR52 = newVal
            in 0xFF30..0xFF3F -> patternRam[address - 0xFF30] = newVal
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Sound")
        }
    }

}