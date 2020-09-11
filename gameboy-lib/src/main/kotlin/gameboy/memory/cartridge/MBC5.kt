package gameboy.memory.cartridge

import gameboy.memory.Memory
import gameboy.utils.setSecondByte
import gameboy.utils.toHexString

class MBC5(romBanks: Int, ramSize: Int, override val hasBattery: Boolean = false) : Memory, MBC {

    override val ram: Array<IntArray>?
    override val rom: Array<IntArray>

    override var currentRomBank = 0
    override var currentRamBank = 0
    override var ramEnabled = false

    var mode = 0

    init {
        reset()

        if (romBanks !in 0..512) {
            throw IllegalArgumentException("Illegal number of ROM banks: $romBanks")
        }
        rom = Array(romBanks) {IntArray(0x4000)}
        ram = when (ramSize) {
            0       -> null
            0x800   -> Array(1) {IntArray(0x800)}
            0x2000  -> Array(1) {IntArray(0x2000)}
            0x8000  -> Array(4) {IntArray(0x2000)}
            0x20000 -> Array(16) {IntArray(0x2000)}
            0x10000 -> Array(8) {IntArray(0x2000)}
            else -> throw IllegalArgumentException("Illegal RAM size: $ramSize")
        }
    }

    override fun reset() {
        super.reset()
        currentRamBank = 0
        currentRomBank = 0
        mode = 0
        ramEnabled = false
    }

    override fun loadRom(value: ByteArray) {
        for (i in value.indices) {
            val bank: Int = i / 0x4000
            val index: Int = i - (bank * 0x4000)

            rom[bank][index] = (value[i].toInt()) and 0xFF
        }
    }

    override fun readRom(address: Int): Int {
        when(address) {
            in 0x0000 until 0x4000 -> {
                if (mode == 1) {
                    val bank = (currentRamBank shl 5) % rom.size
                    return this.rom[bank][address]
                }

                return this.rom[0][address]
            }
            in 0x4000 until 0x8000 -> {
                var romBank = currentRomBank or (currentRamBank shl 5)
                romBank %= rom.size
                return this.rom[romBank][address - 0x4000]
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
        }
    }

    override fun writeRom(address: Int, value: Int) {
        when(address) {
            // RAM Enable
            in 0x0000 until 0x2000 -> ramEnabled = (value and 0x0F) == 0x0A

            // ROM Bank Number lower 8 bits
            in 0x2000 until 0x3000 -> {
                val newVal = value and 0xFF
                val newRomBank = (currentRomBank and 0xFF) or newVal

                currentRomBank = newRomBank % rom.size
            }

            // ROM Bank Number upper bit
            in 0x3000 until 0x4000 -> {
                val newVal = value and 0b00000001
                val newRomBank = setSecondByte(currentRomBank, newVal)

                currentRomBank = newRomBank % rom.size
            }

            // RAM Bank Number or Upper Bits of ROM Bank Number
            in 0x4000 until 0x6000 -> {
                val newVal = value and 0b00000011
                /*
                if (mode == 0) {
                    val newRomBank = currentRomBank or (newVal shl 5)
                    currentRomBank = newRomBank % rom.size

                } else {
                    // Check whether we actually have RAM and whether the selected bank actually exists
                    if (ram != null && newVal < ram.size) {
                        currentRamBank = newVal
                    }
                }*/
                currentRamBank = newVal
            }

            // ROM/RAM Mode Select
            in 0x6000 until 0x8000 -> {
                mode = value and 0b00000001
            }
        }
    }

    override fun readRam(address: Int): Int {
        if (address !in 0xA000 until 0xC000) {
            throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
        }

        if (ram == null || !ramEnabled) {
            return 0xFF
        }

        val newAddress = address - 0xA000
        return if (mode == 0) {
            this.ram[0][newAddress]
        } else {
            if (currentRamBank >= ram.size) {
                this.ram[0][newAddress]
            } else {
                this.ram[currentRamBank][newAddress]
            }
        }
    }

    override fun writeRam(address: Int, value: Int) {
        if (address !in 0xA000 until 0xC000) {
            throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
        }

        if (ram == null || !ramEnabled) {
            return
        }

        val newAddress = address - 0xA000
        val newVal = value and 0xFF
        if (mode == 0) {
            this.ram[0][newAddress] = newVal
        } else {
            if (currentRamBank >= ram.size) {
                this.ram[0][newAddress] = newVal
            } else {
                this.ram[currentRamBank][newAddress] = newVal
            }
        }
    }

    override fun toString(): String {
        return "MBC1, ${rom.size} banks of ROM, ${ram?.size ?: 0} banks of RAM, Battery: $hasBattery"
    }
}