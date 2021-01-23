package gameboy.memory.io.sound

import gameboy.memory.Memory

abstract class SoundChannel : Memory {

    var enabled = false
    protected abstract val lengthCounter: LengthCounter
    protected val volumeEnvelope = VolumeEnvelope()
    protected var lastOutput = 0

    abstract fun tick(): Int

    open fun powerOff() {
        enabled = false
        lengthCounter.reset()
        volumeEnvelope.reset()
    }

    protected open fun trigger() {
        enabled = true
        volumeEnvelope.trigger()

        if (!volumeEnvelope.getDac()) {
            enabled = false
        }
    }

    override fun reset() {
        enabled = false
        lastOutput = 0
        lengthCounter.reset()
        volumeEnvelope.reset()
    }
}