package memory.io.sound

import GameBoy
import utils.Log
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.SourceDataLine

class SoundOutput {
    private val SAMPLE_RATE = 131072f
    private val SAMPLES_PER_FRAME = SAMPLE_RATE.toInt() / 60
    private val AUDIO_FORMAT = AudioFormat(SAMPLE_RATE, 8, 2, false, false)

    private var counter = 0
    private var divider = GameBoy.TICKS_PER_SEC / SAMPLE_RATE.toInt()

    private var masterBuffer: ByteArray = ByteArray(6 * SAMPLES_PER_FRAME)
    private var tempBuffer: ByteArray = ByteArray(3 * SAMPLES_PER_FRAME)
    private var bufferIndex = 0

    private var line : SourceDataLine? = null

    fun start() {
        if (line != null) {
            Log.d("Sound already started")
            return
        }
        Log.d("Start sound")
        try {
            line = AudioSystem.getSourceDataLine(AUDIO_FORMAT)
            line?.open(AUDIO_FORMAT)
            line?.start()
        } catch (e: LineUnavailableException) {
            throw RuntimeException(e)
        }

        //buffer = ByteArray(line!!.bufferSize)
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

       // buffer[bufferIndex++] = left.toByte()
        //buffer[bufferIndex++] = right.toByte()

        //if (bufferIndex >= BUFFER_SIZE) {
            val samplesToWrite = Math.min(line!!.available(), bufferIndex)
        //    line?.write(buffer, 0, samplesToWrite)
            bufferIndex = 0
        //}
    }
}