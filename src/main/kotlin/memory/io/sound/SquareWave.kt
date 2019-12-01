package memory.io.sound

abstract class SquareWave : SoundChannel() {
    protected val dutyCycles = arrayOf(0b00000001, 0b10000001, 0b10000111, 0b01111110)
    protected var duty = 0
}