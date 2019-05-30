package memory.io.sound

import java.io.Serializable

class WaveChannel : SoundChannel, Serializable {
    override fun handleByte(location: Int, value: Int) {

    }

    override fun tick(soundBuffer: ByteArray, samples: Int): Boolean {
        return false
    }

}