package gameboy.memory.io.sound

import gameboy.memory.Mmu

class SquareWave2 : SquareWave() {

    override val lengthCounter = LengthCounter(64, this)
    private var frequency = 0

    init {
        reset()
    }

    override fun reset() {
        super.reset()
        duty = 0
        frequency = 0
    }

    override fun powerOff() {
        super.powerOff()
        frequency = 0
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF

        when (address) {
            Mmu.NR23 -> frequency = (frequency and 0b11100000000) or newVal
            Mmu.NR24 -> {
                frequency = (frequency and 0b11111111) or ((newVal and 0b111) shl 8)
                super.writeByte(address, newVal)
            }
            else -> super.writeByte(address, newVal)
        }
    }

    override fun getFrequency(): Int {
        return 2048 - frequency
    }
}