package gameboy.memory.io.graphics

import gameboy.GameBoy
import java.util.*

class Lcd {

    private val bufferSize = GameBoy.SCREEN_WIDTH * GameBoy.SCREEN_HEIGHT * 3
    private var currentIndex = 0
    private val buffer = ByteArray(bufferSize)
    private val listeners = LinkedList<VSyncListener>()

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

        for (listener in listeners)
            listener.vsync(buffer)

        currentIndex = 0
    }

    fun addListener(output: VSyncListener) {
        listeners.add(output)
    }
}