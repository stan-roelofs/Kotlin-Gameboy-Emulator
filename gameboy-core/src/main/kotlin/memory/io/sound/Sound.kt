package memory.io.sound

import memory.Memory
import memory.Mmu
import utils.getBit
import utils.setBit
import utils.toHexString

class Sound : Memory {

    private val square1 = SquareWave1()
    private val square2 = SquareWave2()
    private val wave = WaveChannel()
    private val noise = NoiseChannel()

    var optionChannelEnables = Array(4) {true}

    private val allChannels: Array<SoundChannel> = arrayOf(square1, square2, wave, noise)

    private var enabled = true

    var output : SoundOutput? = null
    private val samples = IntArray(4)

    private var vinLeft = false
    private var vinRight = false
    private var volumeLeft = 0
    private var volumeRight = 0
    private var leftEnables = Array(4) {false}
    private var rightEnables = Array(4) {false}

    init {
        reset()
    }

    override fun reset() {
        vinLeft = false
        vinRight = false
        volumeLeft = 0b111
        volumeRight = 0b111

        leftEnables.fill(true)
        rightEnables[0] = true
        rightEnables[1] = true
        rightEnables[2] = false
        rightEnables[3] = false

        for (channel in allChannels) {
            channel.reset()
        }

        enabled = true
        samples.fill(0)

        output?.reset()
    }

    fun tick(cycles: Int) {
        for (i in 0 until 4) {
            samples[i] = if (optionChannelEnables[i]) allChannels[i].tick(cycles) else 0
        }

        if (!enabled) {
            return
        }

        var left = 0
        var right = 0

        for (i in 0 until 4) {
            if (leftEnables[i]) {
                left += samples[i]
            }
            if (rightEnables[i]) {
                right += samples[i]
            }
        }
        left /= 4
        right /= 4

        // Bits 4..6 contain left volume
        left *= volumeLeft// + 1
        // Bits 0..2 contain right volume
        right *= volumeRight// + 1

        output?.play(left, right)
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR10,
            Mmu.NR11,
            Mmu.NR12,
            Mmu.NR13,
            Mmu.NR14 -> {
                square1.readByte(address)
            }
            Mmu.NR21,
            Mmu.NR22,
            Mmu.NR23,
            Mmu.NR24 -> {
                square2.readByte(address)
            }
            Mmu.NR30,
            Mmu.NR31,
            Mmu.NR32,
            Mmu.NR33,
            Mmu.NR34,
            in 0xFF30..0xFF3F -> {
                wave.readByte(address)
            }
            Mmu.NR41,
            Mmu.NR42,
            Mmu.NR43,
            Mmu.NR44 -> {
                noise.readByte(address)
            }
            Mmu.NR50 -> {
                var result = 0
                result = setBit(result, 7, this.vinLeft)
                result = result or (this.volumeLeft shl 4)
                result = setBit(result, 3, this.vinRight)
                result = result or (this.volumeRight)
                result
            }
            Mmu.NR51 -> {
                var result = 0
                result = setBit(result, 7, leftEnables[3])
                result = setBit(result, 6, leftEnables[2])
                result = setBit(result, 5, leftEnables[1])
                result = setBit(result, 4, leftEnables[0])
                result = setBit(result, 3, rightEnables[3])
                result = setBit(result, 2, rightEnables[2])
                result = setBit(result, 1, rightEnables[1])
                result = setBit(result, 0, rightEnables[0])
                result
            }
            Mmu.NR52 -> {
                // Bits 0-3 are statuses of channels (1, 2, wave, noise)
                var result = 0b01110000 // Bits 4-6 are unused
                if (square1.enabled) {
                    result = setBit(result, 0)
                }
                if (square2.enabled) {
                    result = setBit(result, 1)
                }
                if (wave.enabled) {
                    result = setBit(result, 2)
                }
                if (noise.enabled) {
                    result = setBit(result, 3)
                }

                // Bit 7 is sound status
                if (enabled) {
                    result = setBit(result, 7)
                }
                result
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Sound")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF

        /* When powered off, all registers (NR10-NR51) are instantly written with zero and any writes to those
         * registers are ignored while power remains off (except on the DMG, where length counters are
         * unaffected by power and can still be written while off)
         */
        if (!enabled) {
            if (address != Mmu.NR52) {
                return
            }
        }

        when(address) {
            Mmu.NR10,
            Mmu.NR11,
            Mmu.NR12,
            Mmu.NR13,
            Mmu.NR14 -> {
                square1.writeByte(address,  value)
            }
            Mmu.NR21,
            Mmu.NR22,
            Mmu.NR23,
            Mmu.NR24 -> {
                square2.writeByte(address,  value)
            }
            Mmu.NR30,
            Mmu.NR31,
            Mmu.NR32,
            Mmu.NR33,
            Mmu.NR34,
            in 0xFF30..0xFF3F -> {
                wave.writeByte(address,  value)
            }
            Mmu.NR41,
            Mmu.NR42,
            Mmu.NR43,
            Mmu.NR44 -> {
                noise.writeByte(address,  value)
            }
            Mmu.NR50 -> {
                this.vinLeft = newVal.getBit(7)
                this.volumeLeft = (newVal shr 4) and 0b111
                this.vinRight = newVal.getBit(3)
                this.volumeRight = newVal and 0b111
            }
            Mmu.NR51 -> {
                this.leftEnables[3] = newVal.getBit(7)
                this.leftEnables[2] = newVal.getBit(6)
                this.leftEnables[1] = newVal.getBit(5)
                this.leftEnables[0] = newVal.getBit(4)
                this.rightEnables[3] = newVal.getBit(3)
                this.rightEnables[2] = newVal.getBit(2)
                this.rightEnables[1] = newVal.getBit(1)
                this.rightEnables[0] = newVal.getBit(0)
            }
            Mmu.NR52 -> {
                val wasEnabled = enabled
                enabled = value.getBit(7)

                if (wasEnabled && !enabled) {
                    for (channel in allChannels) {
                        channel.powerOff()
                    }

                    vinLeft = false
                    volumeLeft = 0
                    vinRight = false
                    volumeRight = 0

                    for (i in 0..3) {
                        this.leftEnables[i] = false
                        this.rightEnables[i] = false
                    }
                }
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Sound")
        }
    }

}