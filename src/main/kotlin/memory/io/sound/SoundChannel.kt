package memory.io.sound

import memory.Memory

abstract class SoundChannel : Memory {

    protected var channelEnabled = false
    protected var dacEnabled = false

    protected val LENGTH_CLOCKS = 16384
    protected val SWEEP_CLOCKS = 32768
    protected val VOL_CLOCKS = 65536

    protected abstract var NR0: Int
    protected abstract var NR1: Int
    protected abstract var NR2: Int
    protected abstract var NR3: Int
    protected abstract var NR4: Int

    abstract fun tick(cycles: Int): Int

    protected abstract fun trigger()

    fun isEnabled(): Boolean {
        return channelEnabled && dacEnabled
    }
}