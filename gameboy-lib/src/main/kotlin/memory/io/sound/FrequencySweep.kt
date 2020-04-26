package memory.io.sound

import gameboy.GameBoy
import utils.getBit
import utils.setBit

class FrequencySweep {

    private val DIVIDER = GameBoy.TICKS_PER_SEC / 128

    private var timer = 0
    var enabled = false
    private var shift = 0
    private var shadowRegister = 0
    private var frequency = 0
    private var period = 0
    private var negate = false
    private var counter = 0

    init {
        reset()
    }

    fun reset() {
        timer = 0
        enabled = false
        shift = 0
        shadowRegister = 0
        frequency = 0
        period = 0
        negate = false
        counter = 0
    }

    fun tick() {
        counter++
        if (counter == DIVIDER) {
            counter = 0

            if (!enabled)
                return

            timer--
            if (timer == 0) {
                timer = if (period == 0) 8 else period

                if (period != 0) {
                    val freq = calculate()

                    // If overflow enabled will be false
                    if (enabled && shift != 0) {
                        frequency = freq
                        shadowRegister = freq

                        calculate()
                    }
                }
            }
        }
    }

    fun setNr10(value : Int) {
        period = (value shr 4) and 0b111 // Bits 654 period
        negate = value.getBit(3)    // Bit 3 negate
        shift = value and 0b111          // Bits 210 shift
    }

    fun setNr13(value: Int) {
        frequency = (frequency and 0b11100000000) or value
    }

    fun setNr14(value: Int) {
        frequency = (frequency and 0b11111111) or ((value and 0b111) shl 8)
    }

    fun getNr10(): Int {
        var result = 0b10000000
        result = result or shift
        result = setBit(result, 3, negate)
        result = result or (period shl 4)
        return result
    }

    fun trigger() {
        shadowRegister = frequency
        timer = if (period == 0) 8 else period
        enabled = period != 0 || shift != 0

        if (shift > 0) {
            frequency = calculate()
        }
    }

    fun getFrequency(): Int {
        return 2048 - frequency
    }

    private fun calculate() : Int {
        var freq = shadowRegister shr shift
        freq = if (negate) {
            shadowRegister - freq
        } else {
            shadowRegister + freq
        }

        if (freq > 2047) {
            enabled = false
        }

        return freq
    }
}