package gameboy.memory.io.graphics.mode

import gameboy.memory.io.graphics.Mode

class Vblank : Mode {

    private val DURATION = 456
    private var ticks = 0

    fun start() {
        ticks = 0
    }

    override fun tick() {
        ticks++
    }

    override fun finished(): Boolean {
        return ticks == DURATION
    }
}