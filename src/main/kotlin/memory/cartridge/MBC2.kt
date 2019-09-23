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
        ramEnabled = false
        ram[0].fill(0)
    }

    override fun loadRom(value: ByteArray) {
        for (i in value.indices) {
            val bank: Int = i / 0x4000
            val index: Int = i - (bank * 0x4000)

            rom[bank][index] = (value[i].toInt()) and 0xFF
        }
    }

    override fun readRom(address: Int): Int {
        return when(address) {
            in 0x0000 until 0x4000 -> {
                this.rom[0][address]
            }
            in 0x4000 until 0x8000 -> {
                this.rom[currentRomBank][address - 0x4000]
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
        }
    }

    override fun writeRom(address: Int, value: Int) {
        when(address) {
            in 0x0000 until 0x4000 -> {
                if (!address.getSecondByte().getBit(0)) {
                    ramEnabled = (value and 0x0F) == 0x0A
                } else {
                    val newVal = value and 0x0f
                    var newRomBank = newVal

                    if (newRomBank == 0) {
                        newRomBank = 1
                    }

                    currentRomBank = newRomBank % rom.size
                }
            }
        }
    }

    override fun readRam(address: Int): Int {
        if (address !in 0xA000 until 0xC000) {
            throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
        }

        if (!ramEnabled) {
            return 0xFF
        }

        val newAddress = (address - 0xA000) % 0x200
        return 0xF0 or (this.ram[0][newAddress] and 0x0F) // Only lower 4 bits are used
    }

    override fun writeRam(address: Int, value: Int) {
        if (address !in 0xA000 until 0xC000) {
            throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
        }

        if (!ramEnabled) {
            return
        }

        val newAddress = (address - 0xA000) % 0x200
        val newVal = value and 0x0F // Only lower 4 bits are used
        this.ram[0][newAddress] = newVal
    }

    override fun toString(): String {
        return "MBC2, ${rom.size} banks of ROM, ${ram.size} banks of RAM, Battery: $hasBattery"
    }
}