package gameboy.memory.io.graphics

interface Mode {
    fun tick()
    fun finished(): Boolean
}