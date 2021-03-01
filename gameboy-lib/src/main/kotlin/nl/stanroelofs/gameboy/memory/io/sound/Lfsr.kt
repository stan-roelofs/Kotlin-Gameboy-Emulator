package nl.stanroelofs.gameboy.memory.io.sound

class Lfsr {
    private var lfsr = 0

    init {
        reset()
    }

    fun reset() {
        lfsr = 0x7fff
    }

    fun nextBit(width7: Boolean): Int {
        val x = lfsr and 1 xor (lfsr and 2 shr 1) != 0
        lfsr = lfsr shr 1
        lfsr = lfsr or if (x) 1 shl 14 else 0
        if (width7) {
            lfsr = lfsr or if (x) 1 shl 6 else 0
        }
        return 1 and lfsr.inv()
    }
}