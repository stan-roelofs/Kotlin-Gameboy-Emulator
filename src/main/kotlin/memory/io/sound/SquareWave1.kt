package memory.io.sound

import memory.Mmu

class SquareWave1 : SquareWave() {

    private var NR0 = 0

    init {
        reset()
    }

    override fun reset() {
        NR0 = 0
        super.reset()
        duty = 0b10
        volumeEnvelope.setNr2(0xF3)
        enabled = true
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR10 -> {
                this.NR0 or 0b10000000
            }
            else -> super.readByte(address)
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.NR10 -> {
                NR0 = newVal
            }
            else -> super.writeByte(address, value)
        }
    }
}