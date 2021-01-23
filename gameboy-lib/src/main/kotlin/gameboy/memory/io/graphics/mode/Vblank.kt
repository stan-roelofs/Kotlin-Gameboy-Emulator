package gameboy.memory.io.graphics.mode

class Vblank : Mode {

    private val DURATION = 455
    private var ticks = 0

    fun start() {
        ticks = 0
    }

    override fun tick() {
        ticks++
    }

    override fun finished(): Boolean {
        return ticks >= DURATION
    }
}