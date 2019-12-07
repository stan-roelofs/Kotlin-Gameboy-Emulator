package memory.io.sound

import GameBoy

class LengthCounter(private val fullLength: Int) {

    private val DIVIDER = GameBoy.TICKS_PER_SEC / 256

    private var length = 0
    private var counter = 0
    var enabled = false

    init {
        reset()
    }

    fun reset() {
        this.enabled = false
        this.counter = GameBoy.TICKS_PER_SEC / 256
        this.length = fullLength
    }

    fun tick() {
        counter++
        if (counter >= DIVIDER) {
            counter = 0
            if (enabled && length > 0) {
                length--
                if (length == 0) {
                    counter = 0
                    enabled = false
                }
            }
        }
    }

    fun setNr1(value: Int) {
        this.length = (value and 0b00111111)
    }

    fun trigger() {
        this.enabled = true
        if (length == 0) {
            length = 64
        }
    }
}