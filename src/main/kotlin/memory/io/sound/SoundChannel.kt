package memory.io.sound

import memory.Memory

abstract class SoundChannel : Memory {

    protected var dacEnabled = false

    protected abstract val lengthCounter: LengthCounter
    protected val volumeEnvelope = VolumeEnvelope()

    protected abstract var NR0: Int
    protected abstract var NR1: Int
    protected abstract var NR2: Int
    protected abstract var NR3: Int
    protected abstract var NR4: Int

    abstract fun tick(cycles: Int): Int

    protected abstract fun trigger()

    fun isEnabled(): Boolean {
        return lengthCounter.enabled && dacEnabled
    }
}