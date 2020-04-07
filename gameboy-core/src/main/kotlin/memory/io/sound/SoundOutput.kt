package memory.io.sound

interface SoundOutput {
    fun reset()
    fun play(left: Byte, right: Byte)
}