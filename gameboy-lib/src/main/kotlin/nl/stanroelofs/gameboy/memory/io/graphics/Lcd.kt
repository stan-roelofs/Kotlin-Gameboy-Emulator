package nl.stanroelofs.gameboy.memory.io.graphics

import nl.stanroelofs.gameboy.GameBoy
import java.util.*

class Lcd {

    private val bufferSize = GameBoy.SCREEN_WIDTH * GameBoy.SCREEN_HEIGHT * 3
    private var currentIndex = 0
    private val buffer = ByteArray(bufferSize)
    private val listeners : MutableCollection<VSyncListener> = LinkedList()

    fun reset() {
        currentIndex = 0
    }

    fun setCurrentLine(value: Int) {
        if (value !in 0..153)
            throw IllegalArgumentException("LY must be between 0 and 153, invalid value: $value")
        currentIndex = value * GameBoy.SCREEN_WIDTH * 3
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