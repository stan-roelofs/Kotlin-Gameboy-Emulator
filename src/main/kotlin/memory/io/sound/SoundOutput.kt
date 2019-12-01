package memory.io.sound

import GameBoy
import utils.Log
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.SourceDataLine

class SoundOutput {
    private val SAMPLE_RATE = 22050f
    private val BUFFER_SIZE = 1024
    private val AUDIO_FORMAT = AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, SAMPLE_RATE, 8, 2, 2, SAMPLE_RATE, false)

    private var counter = 0
    private var divider : Int = 0

    private var bufferIndex = 0

    private var line : SourceDataLine? = null
    private var buffer = ByteArray(BUFFER_SIZE)

    fun start() {
        if (line != null) {
            Log.d("Sound already started")
            return
        }
        Log.d("Start sound")
        try {
            line = AudioSystem.getSourceDataLine(AUDIO_FORMAT)
            line?.open(AUDIO_FORMAT, BUFFER_SIZE)
        } catch (e: LineUnavailableException) {
            throw RuntimeException(e)
        }
        line?.start()

        buffer = ByteArray(line!!.bufferSize)
        divider = GameBoy.TICKS_PER_SEC / AUDIO_FORMAT.sampleRate.toInt()
    }

    fun stop() {
        if (line == null) {
            Log.d("Can't stop - sound wasn't started")
        }
        Log.d("Stop sound")
        line?.drain()
        line?.stop()
        line = null
    }

    fun play(left: Int, right: Int) {
        if (counter++ != 0) {
            counter %= divider
            return
        }

        if (left !in 0..0xFF || right !in 0..0xFF) {
            Log.e("Left or right not in range")
        }

        buffer[bufferIndex++] = left.toByte()
        buffer[bufferIndex++] = right.toByte()

        if (bufferIndex > BUFFER_SIZE / 2) {
            line?.write(buffer, 0, bufferIndex)
            bufferIndex = 0
        }
    }
}