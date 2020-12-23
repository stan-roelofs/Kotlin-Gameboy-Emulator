package gameboy.memory.io.graphics

import gameboy.GameBoy

class Lcd {

    private val bufferSize = GameBoy.SCREEN_WIDTH * GameBoy.SCREEN_HEIGHT * 3
    private var currentIndex = 0
    private val buffer = ByteArray(bufferSize)
    var output : ScreenOutput? = null

    fun reset() {
        currentIndex = 0
    }

    fun pushPixel(r: Int, g: Int, b: Int) {
        buffer[currentIndex] = r.toByte()
        buffer[++currentIndex] = g.toByte()
        buffer[++currentIndex] = b.toByte()
        ++currentIndex
    }

    fun display() {
        if (currentIndex != bufferSize)
            throw IllegalStateException()

        output?.render(buffer)
    }
}