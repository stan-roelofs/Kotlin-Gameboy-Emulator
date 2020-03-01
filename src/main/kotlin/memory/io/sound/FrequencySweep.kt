package memory.io.sound

class FrequencySweep {

    private var timer = 0
    private var enabled = false
    private var shift = 0
    private var shadowRegister = 0
    private var frequency = 0
    private var period = 0
    private var negate = false
    private var overflow = false

    fun reset() {

    }

    fun tick() {

    }

    fun setNr10(value : Int) {

    }

    fun getNr10(): Int {

    }

    fun setShadow(value: Int) {
        shadowRegister = value
    }

    fun trigger() {
        shadowRegister = frequency
        timer = period
        enabled = period != 0 || shift != 0

        if (shift != 0) {
            frequency = calculate()
        }
    }

    fun setFrequency(value : Int) {
        frequency = value
    }

    fun getFrequency(): Int {
        return frequency
    }

    private fun calculate(): Int {
        var freq = shadowRegister shr shift
        freq = if (negate) {
            shadowRegister - freq
        } else {
            shadowRegister + freq
        }

        if (freq > 2047) {
            overflow = true
        }
        return freq
    }
}