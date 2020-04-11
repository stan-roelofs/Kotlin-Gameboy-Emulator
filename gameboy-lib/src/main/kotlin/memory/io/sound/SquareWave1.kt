package memory.io.sound

import memory.Mmu

class SquareWave1 : SquareWave() {

    private val frequencySweep = FrequencySweep()

    init {
        reset()
    }

    override fun reset() {
        super.reset()
        duty = 0b10
        volumeEnvelope.setNr2(0xF3)
        enabled = true
        frequencySweep.reset()
    }

    override fun tick(cycles: Int): Int {
        frequencySweep.tick()
        return super.tick(cycles)
    }

    override fun readByte(address: Int): Int {
        return if (address == Mmu.NR10) {
            frequencySweep.getNr10()
        } else {
            super.readByte(address)
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF

        if (address == Mmu.NR10) {
            frequencySweep.setNr10(newVal)
        } else {
            super.writeByte(address, value)
        }
    }

    override fun getFrequency(): Int {
        return 2048 - frequencySweep.getFrequency()
    }

    override fun trigger() {
        frequencySweep.trigger()
        super.trigger()
    }
}