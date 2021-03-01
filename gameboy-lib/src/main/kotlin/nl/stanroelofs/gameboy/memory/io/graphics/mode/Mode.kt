package nl.stanroelofs.gameboy.memory.io.graphics.mode

interface Mode {
    fun tick()
    fun finished(): Boolean
}