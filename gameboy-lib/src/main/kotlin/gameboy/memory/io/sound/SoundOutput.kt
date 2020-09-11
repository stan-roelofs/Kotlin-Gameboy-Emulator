package gameboy.memory.io.sound

/** Interface for a class which outputs audio */
interface SoundOutput {
    /** Initialize the output */
    fun initialize()

    /** Dispose resources */
    fun dispose()

    /** Reset to initial values */
    fun reset()

    /** Play an audio sample
     * @param left left channel value
     * @param right right channel value
     */
    fun play(left: Byte, right: Byte)
}