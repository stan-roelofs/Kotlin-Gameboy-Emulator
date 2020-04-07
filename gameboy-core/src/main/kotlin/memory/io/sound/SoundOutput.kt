package memory.io.sound

interface SoundOutput {
    fun reset()
    fun play(left: Int, right: Int)
}