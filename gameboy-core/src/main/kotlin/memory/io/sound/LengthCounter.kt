package memory.io.sound

import GameBoy
import utils.getBit

class LengthCounter(private val fullLength: Int) {

    private val DIVIDER = GameBoy.TICKS_PER_SEC / 256

    var length = 0
        private set

    private var counter = 0
    var enabled = false
        private set

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
        val wasEnabled = enabled
        enabled = value.getBit(6)

        /* Extra length clocking occurs when writing to NRx4 when the frame sequencer's next step is one that doesn't clock the length counter.
         * In this case, if the length counter was PREVIOUSLY disabled and now enabled and the length counter is not zero, it is decremented.
         */
        if (!wasEnabled && enabled && length != 0) {
            if (counter < DIVIDER / 2) {
                length--
            }
        }

        if (value.getBit(7) && length == 0) {
            length = fullLength

            /* If a channel is triggered when the frame sequencer's next step is one that doesn't clock the length counter and the
             * length counter is now enabled and length is being set to 64 (256 for wave channel) because it was previously zero,
             * it is set to 63 instead (255 for wave channel).
             */
            if (counter < DIVIDER / 2 && enabled) {
                length--
            }
        }
    }
}