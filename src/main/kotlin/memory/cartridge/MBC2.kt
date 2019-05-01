package memory.cartridge

import memory.Memory
import utils.getBit
import utils.getSecondByte
import utils.toHexString

class MBC2(romBanks: Int, override val hasBattery: Boolean = false) : Memory, MBC {

    override val ram: Array<IntArray>
    override val rom: Array<IntArray>

    override var currentRomBank = 1
    override var currentRamBank = 0
    override var ramEnabled = false

    var mode = 0

    init {
        if (romBanks !in 0..16) {
            throw IllegalArgumentException("Illegal number of ROM banks: $romBanks")
        }
        rom = Array(romBanks) {IntArray(0x4000)}
        ram = Array(1) {IntArray(0x200)}
    }

    override fun reset() {
        super.reset()
        currentRamBank = 0
        currentRomBank = 1
        mode = 0
        ramEnabled = false
    }

    override fun loadRom(value: ByteArray) {
        for (i in 0 until value.size) {
            val bank: Int = i / 0x4000
            val index: Int = i - (bank * 0x4000)

            rom[bank][index] = (value[i].toInt()) and 0xFF
        }
    }

    override fun readRom(address: Int): Int {
        when(address) {
            in 0x0000 until 0x4000 -> {
                return this.rom[0][address]
            }
            in 0x4000 until 0x8000 -> {
                return this.rom[currentRomBank][address - 0x4000]
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
        }
    }

    override fun writeRom(address: Int, value: Int) {
        when(address) {
            // RAM Enable
            in 0x0000 until 0x2000 -> ramEnabled = (value and 0x0F) == 0x0A

            // ROM Bank Number lower 5 bits
            in 0x2000 until 0x4000 -> {
                // The least significant bit of address must be set to select a rom bank
                if (!address.getSecondByte().getBit(0)) {
                    return
                }


                // Only use lower bits, since romBanks in 0..15
                val newVal = value and 0x0f
                var newRomBank = newVal

                if (newRomBank == 0) {
                    newRomBank = 1
                }

                currentRomBank = newRomBank
            }
        }
    }

    override fun readRam(address: Int): Int {
        if (address !in 0xA000 until 0xA200) {
            throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
        }

        if (!ramEnabled) {
            return 0xFF
        }

        val newAddress = address - 0xA000
        return this.ram[0][newAddress] and 0x0f // Only lower 4 bits are used
    }

    override fun writeRam(address: Int, value: Int) {
        if (address !in 0xA000 until 0xA200) {
            throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
        }

        if (!ramEnabled) {
            return
        }

        val newAddress = address - 0xA000
        val newVal = value and 0x0F // Only lower 4 bits are used
        this.ram[0][newAddress] = newVal
    }
}