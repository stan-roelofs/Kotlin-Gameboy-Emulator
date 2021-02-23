import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.AudioDevice
import com.badlogic.gdx.utils.GdxRuntimeException
import gameboy.GameBoy
import gameboy.memory.io.sound.SoundOutput

class SoundOutputGdx : SoundOutput {

    private val SAMPLE_RATE = 22050
    private val BUFFER_SIZE = 1024

    private var device : AudioDevice? = null
    private var counter = 0
    private var divider = GameBoy.TICKS_PER_SEC / SAMPLE_RATE
    private var bufferIndex = 0
    private val buffer = ShortArray(BUFFER_SIZE)
    private var audioThread : AudioThread? = null

    override fun initialize() {
        device = try {
            Gdx.audio.newAudioDevice(SAMPLE_RATE, false)
        } catch (e: GdxRuntimeException) {
            null
        }

        if (device != null) {
            audioThread = AudioThread(device!!)
            audioThread!!.start()
        }
    }

    override fun reset() {
        counter = 0
        bufferIndex = 0
        buffer.fill(0)
    }

    override fun play(left: Byte, right: Byte) {
        if (counter++ != 0) {
            counter %= divider
            return
        }

        buffer[bufferIndex++] = (left * 255).toShort()
        buffer[bufferIndex++] = (right * 255).toShort()

        if (bufferIndex >= BUFFER_SIZE) {
            audioThread?.buffer = buffer
            audioThread?.play = true

            bufferIndex = 0
        }
    }

    override fun dispose() {
        device?.dispose()
    }
}

class AudioThread(private val device: AudioDevice) : Thread() {

    @Volatile var play = false
    @Synchronized set
    var buffer = ShortArray(1024)

    override fun run() {
        while (true) {
            if (play) {
                device.writeSamples(buffer, 0, 1024)
                play = false
            }
        }
    }
}