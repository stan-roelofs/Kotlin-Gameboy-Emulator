package memory.io.sound

import memory.Memory

abstract class SoundChannel : Memory {

    var enabled = false
    protected abstract val lengthCounter: LengthCounter
    protected val volumeEnvelope = VolumeEnvelope()
    protected var lastOutput = 0

    protected abstract var NR0: Int
    protected abstract var NR1: Int
    protected abstract var NR2: Int
    protected abstract var NR3: Int
    protected abstract var NR4: Int

    abstract fun tick(cycles: Int): Int

    protected open fun trigger() {
        enabled = true
        lengthCounter.trigger()
        volumeEnvelope.trigger()

        if (!volumeEnvelope.getDac()) {
            enabled = false
        }
    }

    override fun reset() {
        lastOutput = 0
        lengthCounter.reset()
        volumeEnvelope.reset()
    }
}