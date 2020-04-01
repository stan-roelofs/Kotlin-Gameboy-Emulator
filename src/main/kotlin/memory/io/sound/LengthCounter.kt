package memory.io.sound

import GameBoy
import utils.getBit

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
        this.length = if (value == 0) fullLength else fullLength - value
    }

    fun setNr4(value: Int) {
        val wasEnabled = enabled;
        enabled = value.getBit(6)

        /* Extra length clocking occurs when writing to NRx4 when the frame sequencer's next step is one that doesn't clock the length counter.
         * In this case, if the length counter was PREVIOUSLY disabled and now enabled and the length counter is not zero, it is decremented.
         */
        if (!wasEnabled && enabled && length != 0) {
            if (counter < DIVIDER / 2) {
                length--
            }
        }
    }

    fun trigger() {
        if (length == 0) {
            length = fullLength
        }
    }
}