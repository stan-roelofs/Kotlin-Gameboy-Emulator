package gameboy.memory.io.graphics.mode

import gameboy.memory.io.graphics.Mode

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