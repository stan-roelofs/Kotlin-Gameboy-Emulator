package memory.io.sound

import memory.Memory

abstract class SoundChannel : Memory {

    var enabled = false
    protected abstract val lengthCounter: LengthCounter
    protected val volumeEnvelope = VolumeEnvelope()
    protected var lastOutput = 0

    abstract fun tick(cycles: Int): Int

    open fun disable() {
        enabled = false
        lengthCounter.reset()
        volumeEnvelope.reset()
    }

    protected open fun trigger() {
        enabled = true
        lengthCounter.trigger()
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