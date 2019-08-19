package memory.io.sound

import java.io.Serializable

abstract class SquareWave : SoundChannel, Serializable {

    private var duty = 0

    private val dutyCycles = arrayOf(0b00000001, 0b10000001, 0b10000111, 0b01111110)

    private var lengthLoad = 0
    private var startingVolume = 0
    private var envelopeAdd = false
    private var envelopePeriod = 0
    private var frequency = 0
    private var playing = false
    private var lengthEnabled = false
    private var lengthCounter = 0

    private var currentVolume = 0
    private var ticks: Long = 0
    private var offset = 0

    private val transition = ByteArray(Sound.SAMPLE_RATE)

    val SAMPLE_RATE = Sound.SAMPLE_RATE

    //tick is 30 hz
    override fun tick(soundBuffer: ByteArray, samplesToWrite: Int): Boolean {
        /*
        if (!this.playing) {
            return false
        }

        ticks++

        if (lengthEnabled) {
            lengthCounter -= 4
            if (lengthCounter <= 0) {
                this.playing = false
            }
        }

        if (envelopePeriod != 0 && ticks % envelopePeriod == 0L) {
            this.currentVolume += if (envelopeAdd) 1 else -1
            if (this.currentVolume < 0) this.currentVolume = 0
            if (this.currentVolume > 15) this.currentVolume = 15
        }

        val waveForm = getWaveform(this.duty)
        val chunkSize = (2048.0 - frequency) / 8.0 / 3.0

        val waveLength = (8 * chunkSize).toInt()

        if (waveLength == 0) return false

        for (i in 0 until samplesToWrite) {
            val samplesFromStart = (i + offset) % waveLength
            val loc = (samplesFromStart / chunkSize).toInt()
            soundBuffer[i] = if (waveForm shr loc and 1 == 1) currentVolume.toByte() else 0.toByte()
        }

        offset = (offset + samplesToWrite) % waveLength
*/
        return false
    }

    //location is 0, 1, 2, 3, 4
    fun handleByte(location: Int, value: Int) {
        var newFrequency = frequency

        when (location) {
            0 -> {
            }
            1 -> {
                this.duty = value shr 6 and 0x3
                this.lengthLoad = 64 - (value and 0x3f)
            }
            2 -> {
                this.startingVolume = value shr 4 and 0xf
                this.currentVolume = this.startingVolume
                this.envelopeAdd = value shr 3 and 1 == 1
                this.envelopePeriod = value and 0x7
            }
            3 -> {
                newFrequency = newFrequency shr 8 shl 8
                newFrequency = newFrequency or (value and 0xff)
            }
            4 -> {
                this.playing = this.playing or (value shr 7 == 1)
                this.lengthEnabled = value shr 6 and 1 == 1
                newFrequency = newFrequency and 0xff
                newFrequency = newFrequency or (value and 0x7 shl 8)
            }
        }//do nothing

        if (newFrequency != frequency) {
            //if the frequency has changed, update the offset so it's (approximately) at the same spot in the wave
            val oldChunkSize = (2048.0 - frequency) / 8.0 / 3.0
            val oldWaveLength = 8 * oldChunkSize
            val newChunkSize = (2048.0 - newFrequency) / 8.0 / 3.0
            val newWaveLength = 8 * newChunkSize

            offset = (offset * newWaveLength / oldWaveLength).toInt()

            frequency = newFrequency
        }

        if (this.lengthEnabled) {
            this.lengthCounter = this.lengthLoad
            //System.out.println(this.lengthCounter);
        }
    }

}