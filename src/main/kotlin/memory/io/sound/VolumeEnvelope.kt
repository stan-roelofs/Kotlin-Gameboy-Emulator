package memory.io.sound

import GameBoy
import utils.getBit

class VolumeEnvelope {

    private val DIVIDER = GameBoy.TICKS_PER_SEC / 64

    private var add = false
    private var startingVolume = 0
    var volume = 0
    private var volumeTimer = 0
    private var period = 0
    private var counter = 0
    var enabled = false

    init {
        reset()
    }

    fun reset() {
        this.enabled = false
        this.counter = 0
        this.volume = 0
        this.startingVolume = 0
        this.volumeTimer = 0
        this.add = false
        this.period = 0
    }

    fun tick() {
        counter++
        if (counter >= DIVIDER) {
            counter = 0
            if (enabled && volumeTimer > 0) {
                volumeTimer--

                val newVolume = if (add) {
                    volume + 1
                } else {
                    volume - 1
                }

                if (newVolume < 0 || newVolume > 15) {
                    enabled = false
                } else {
                    volume = newVolume
                }
            }
        }
    }

    fun setNr2(value: Int) {
        this.startingVolume = (value shr 4) and 0b1111
        this.add = value.getBit(3)
        this.period = value and 0b111
    }

    fun trigger() {
        this.volume = startingVolume
        this.volumeTimer = period
        this.enabled = true
    }
}