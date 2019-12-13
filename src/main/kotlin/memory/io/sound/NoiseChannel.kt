package memory.io.sound

import memory.Mmu
import utils.getBit
import utils.setBit
import utils.toHexString

class NoiseChannel : SoundChannel() {

    private val lfsr = Lfsr()
    private val polynomialCounter = PolynomialCounter()
    override val lengthCounter = LengthCounter(64)
    override var NR0 = 0
    override var NR1 = 0
    override var NR2 = 0
    override var NR3 = 0
    override var NR4 = 0

    init {
        reset()
    }

    override fun trigger() {
        super.trigger()
        lfsr.reset()
    }

    override fun tick(cycles: Int): Int {
        lengthCounter.tick()
        if (lengthCounter.enabled && lengthCounter.length == 0) {
            enabled = false
        }

        volumeEnvelope.tick()

        if (polynomialCounter.tick()) {
            lastOutput = lfsr.nextBit(polynomialCounter.width7)
        }

        val volume = volumeEnvelope.volume
        return lastOutput * volume
    }

    override fun reset() {
        super.reset()
        lengthCounter.setNr1(0xFF)
        polynomialCounter.reset()
        lfsr.reset()
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR41 -> 0xFF
            Mmu.NR42 -> volumeEnvelope.getNr2()
            Mmu.NR43 -> polynomialCounter.getNr43()
            Mmu.NR44 -> {
                var result = 0b10111111
                result = setBit(result, 6, lengthCounter.enabled)
                result
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to NoiseChannel")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.NR41 -> {
                lengthCounter.setNr1(value)
            }
            Mmu.NR42 -> {
                volumeEnvelope.setNr2(newVal)

                if (!volumeEnvelope.getDac()) {
                    enabled = false
                }
            }
            Mmu.NR43 -> {
                polynomialCounter.setNr43(newVal)
            }
            Mmu.NR44 -> {
                lengthCounter.enabled = newVal.getBit(6)

                if (newVal.getBit(7)) {
                    trigger()
                }
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to NoiseChannel")
        }
    }
}