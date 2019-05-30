package memory.io.sound

interface SoundChannel {
    fun handleByte(location: Int, value: Int)
    fun tick(soundBuffer: ByteArray, samples: Int): Boolean
}