package memory.io.sound

abstract class SquareWave : SoundChannel() {
    protected val dutyCycles = arrayOf(0b00000001, 0b10000001, 0b10000111, 0b01111110)
    protected var duty = 0

    protected fun getFrequency(): Int {
        return 2048 - (NR3 or (NR4 and 0b111 shl 8))
    }
}