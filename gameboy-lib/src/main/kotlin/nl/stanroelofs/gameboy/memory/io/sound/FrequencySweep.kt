package nl.stanroelofs.gameboy.memory.io.sound

import nl.stanroelofs.gameboy.GameBoy
import nl.stanroelofs.gameboy.utils.getBit
import nl.stanroelofs.gameboy.utils.setBit

class FrequencySweep(private val soundChannel : SoundChannel) {

    private val DIVIDER = GameBoy.TICKS_PER_SEC / 128

    private var timer = 0
    private var sweepEnabled = false
    private var shift = 0
    private var shadowRegister = 0
    private var frequency = 0
    private var period = 0
    private var negate = false
    private var counter = 0

    /* Clearing the sweep negate mode bit in NR10 after at least one sweep calculation has been made
     * using the negate mode since the last trigger causes the channel to be immediately disabled.
     */
    private var calculationMade = false

    init {
        reset()
    }

    fun powerOn() {
        counter %= 8192
    }

    fun reset() {
        timer = 0
        sweepEnabled = false
        shift = 0
        shadowRegister = 0
        frequency = 0
        period = 0
        negate = false
        counter = 0
        calculationMade = false
    }

    fun tick() {
        counter++
        if (counter == DIVIDER) {
            counter = 0

            if (!sweepEnabled)
                return

            timer--
            if (timer == 0) {
                timer = if (period == 0) 8 else period

                if (period != 0) {
                    val freq = calculate()

                    // If overflow enabled will be false
                    if (sweepEnabled && shift != 0) {
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
        val newNegate = value.getBit(3)    // Bit 3 negate
        shift = value and 0b111          // Bits 210 shift

        if (newNegate && !negate) {
            calculationMade = false
        }

        if (calculationMade && negate && !newNegate) {
            sweepEnabled = false
            soundChannel.enabled = false
        }

        negate = newNegate
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
        sweepEnabled = period != 0 || shift != 0
        calculationMade = false

        if (shift > 0) {
            frequency = calculate()
        }
    }

    fun getFrequency(): Int {
        return 2048 - frequency
    }

    private fun calculate() : Int {
        calculationMade = true

        var freq = shadowRegister shr shift
        freq = if (negate) {
            shadowRegister - freq
        } else {
            shadowRegister + freq
        }

        if (freq > 2047) {
            sweepEnabled = false
            soundChannel.enabled = false
        }

        return freq
    }
}