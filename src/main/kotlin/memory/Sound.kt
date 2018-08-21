package memory

import utils.toHexString

class Sound : Memory {

    private var NR10 = 0
    private var NR11 = 0
    private var NR12 = 0
    private var NR13 = 0
    private var NR14 = 0

    private var NR21 = 0
    private var NR22 = 0
    private var NR23 = 0
    private var NR24 = 0

    private var NR30 = 0
    private var NR31 = 0
    private var NR32 = 0
    private var NR33 = 0
    private var NR34 = 0

    private var NR41 = 0
    private var NR42 = 0
    private var NR43 = 0
    private var NR44 = 0

    private var NR50 = 0
    private var NR51 = 0
    private var NR52 = 0

    private val patternRam = IntArray(0xF)

    override fun reset() {
        NR10 = 0x80
        NR11 = 0xBF
        NR12 = 0xF3
        NR13 = 0
        NR14 = 0xBF

        NR21 = 0x3F
        NR22 = 0
        NR23 = 0
        NR24 = 0xBF

        NR30 = 0x7F
        NR31 = 0xFF
        NR32 = 0x9F
        NR33 = 0xBF
        NR34 = 0

        NR41 = 0xFF
        NR42 = 0
        NR43 = 0
        NR44 = 0

        NR50 = 0x77
        NR51 = 0xF3
        NR52 = 0xF1

        patternRam.fill(0)
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.NR10 -> this.NR10 or 0b10000000 // Bit 7 unused
            Mmu.NR11 -> this.NR11 or 0b00111111 // Only bits 6-7 can be read
            Mmu.NR12 -> this.NR12
            Mmu.NR13 -> this.NR13
            Mmu.NR14 -> this.NR14 or 0b10111111 // Only bit 6 can be read
            Mmu.NR21 -> this.NR21 or 0b00111111 // Only bits 6-7 can be read
            Mmu.NR22 -> this.NR22
            Mmu.NR23 -> this.NR23
            Mmu.NR24 -> this.NR24 or 0b10111111 // Only bit 6 can be read
            Mmu.NR30 -> this.NR30 or 0b01111111 // Only bit 7 can be read
            Mmu.NR31 -> this.NR31
            Mmu.NR32 -> this.NR32 or 0b10011111 // Only bits 5-6 can be read
            Mmu.NR33 -> this.NR33
            Mmu.NR34 -> this.NR34 or 0b10111111 // Only bit 6 can be read
            Mmu.NR41 -> this.NR41 or 0b11000000 // Bits 6-7 unused
            Mmu.NR42 -> this.NR42
            Mmu.NR43 -> this.NR43
            Mmu.NR44 -> this.NR44 or 0b10111111 // Only bit 6 can be read
            Mmu.NR50 -> this.NR50
            Mmu.NR51 -> this.NR51
            Mmu.NR52 -> this.NR52 or 0b01110000 // Bits 4-6 unused
            in 0xFF30..0xFF3F -> patternRam[address - 0xFF30]
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Sound")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.NR10 -> this.NR10 = newVal
            Mmu.NR11 -> this.NR11 = newVal
            Mmu.NR12 -> this.NR12 = newVal
            Mmu.NR13 -> this.NR13 = newVal
            Mmu.NR14 -> this.NR14 = newVal
            Mmu.NR21 -> this.NR21 = newVal
            Mmu.NR22 -> this.NR22 = newVal
            Mmu.NR23 -> this.NR23 = newVal
            Mmu.NR24 -> this.NR24 = newVal
            Mmu.NR30 -> this.NR30 = newVal
            Mmu.NR31 -> this.NR31 = newVal
            Mmu.NR32 -> this.NR32 = newVal
            Mmu.NR33 -> this.NR33 = newVal
            Mmu.NR34 -> this.NR34 = newVal
            Mmu.NR41 -> this.NR41 = newVal
            Mmu.NR42 -> this.NR42 = newVal
            Mmu.NR43 -> this.NR43 = newVal
            Mmu.NR44 -> this.NR44 = newVal
            Mmu.NR50 -> this.NR50 = newVal
            Mmu.NR51 -> this.NR51 = newVal
            Mmu.NR52 -> this.NR52 = newVal
            in 0xFF30..0xFF3F -> patternRam[address - 0xFF30] = newVal
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Sound")
        }
    }

}