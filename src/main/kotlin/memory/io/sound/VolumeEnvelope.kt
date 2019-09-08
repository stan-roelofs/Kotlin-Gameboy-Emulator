package memory.io.sound

import utils.getBit

class VolumeEnvelope {

    private var initialVolume = 0
    private var envelopeDirection = 0
    private var sweep = 0
    private var volume = 0
    private var i = 0
    private var finished = false

    fun setNr2(value: Int) {
        initialVolume = value shr 4
        envelopeDirection = if (value.getBit(4)) 1 else -1
        sweep = value and 0b111
    }

    fun isEnabled(): Boolean {
        return sweep > 0
    }

    fun start() {
        finished = true
        i = 8192
    }

    fun trigger() {
        volume = initialVolume
        i = 0
        finished = false
    }

    fun tick() {
        if (finished) {
            return
        }
        if (volume == 0 && envelopeDirection == -1 || volume == 15 && envelopeDirection == 1) {
            finished = true
            return
        }
        if (++i == sweep * 4194304 / 64) {
            i = 0
            volume += envelopeDirection
        }
    }

    fun getVolume(): Int {
        return if (isEnabled()) {
            volume
        } else {
            initialVolume
        }
    }
}
