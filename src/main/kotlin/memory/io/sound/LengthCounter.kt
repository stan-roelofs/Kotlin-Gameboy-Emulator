package memory.io.sound

import GameBoy

class LengthCounter(private val fullLength: Int) {

    private val DIVIDER = GameBoy.TICKS_PER_SEC / 256

    var length = 0
    private var counter = 0
    var enabled = false

    init {
        reset()
    }

    fun reset() {
        this.enabled = false
        this.counter = 0
        this.length = fullLength
    }

    fun tick() {
        counter++
        if (counter == DIVIDER) {
            counter = 0
            if (enabled && length > 0) {
                length--
            }
        }
    }

    fun setNr1(value: Int) {
        this.length = if (value == 0) fullLength else value
    }

    fun trigger() {
        if (length == 0) {
            length = fullLength
        }
    }
}