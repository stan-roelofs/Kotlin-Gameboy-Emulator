package gameboy.memory.io.graphics

import gameboy.GameBoy

class Lcd {

    private val bufferSize = GameBoy.SCREEN_WIDTH * GameBoy.SCREEN_HEIGHT * 3
    var currentIndex = 0
        private set

    val lastBuffer = IntArray (bufferSize)
    private val buffer = IntArray (bufferSize)

    fun reset() {
        currentIndex = 0
    }

    fun pushPixel(r: Int, g: Int, b: Int) {
        buffer[currentIndex] = r
        buffer[++currentIndex] = g
        buffer[++currentIndex] = b
        ++currentIndex
    }

    fun display() {
        if (currentIndex != bufferSize)
            throw IllegalStateException()

        buffer.copyInto(lastBuffer)
    }
}