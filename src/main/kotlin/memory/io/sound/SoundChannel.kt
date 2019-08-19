package memory.io.sound

import memory.Memory

interface SoundChannel : Memory {
    override fun readByte(address: Int): Int
    override fun writeByte(address: Int, value: Int)

    fun tick(soundBuffer: ByteArray, samples: Int): Boolean
}