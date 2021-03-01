package nl.stanroelofs.gameboy.memory.io.graphics.mode

class Hblank : Mode {

    private val DURATION = 455
    private var ticks = 0

    fun start(ticksInLine: Int) {
        ticks = ticksInLine
    }

    override fun tick() {
        ticks++
    }

    override fun finished(): Boolean {
        return ticks >= DURATION
    }
}