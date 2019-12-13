package memory.io.sound

class SquareWave2 : SquareWave() {
    init {
        reset()
    }

    override fun reset() {
        super.reset()
        duty = 0
    }
}