package gameboy.memory.io.graphics

class Hblank : Mode {

    private val DURATION = 456
    private var ticks = 0

    fun start(ticksInLine: Int) {
        ticks = ticksInLine
    }

    override fun tick() {
        ticks++
    }

    override fun finished(): Boolean {
        return ticks == DURATION
    }
}