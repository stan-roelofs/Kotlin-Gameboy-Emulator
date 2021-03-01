package nl.stanroelofs.gameboy.memory.cartridge

import nl.stanroelofs.gameboy.memory.Memory
import nl.stanroelofs.gameboy.utils.getBit
import nl.stanroelofs.gameboy.utils.setBit
import nl.stanroelofs.gameboy.utils.toHexString

class MBC5(romBanks: Int, ramSize: Int, override val hasBattery: Boolean = false) : Memory, MBC {

    override val ram: Array<IntArray>?
    override val rom: Array<IntArray>

    override var currentRomBank = 1
    override var currentRamBank = 0
    override var ramEnabled = false

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
        currentRomBank = 1
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
        return when(address) {
            in 0x0000 until 0x4000 -> {
                this.rom[0][address]
            }
            in 0x4000 until 0x8000 -> {
                val bank = currentRomBank % rom.size
                this.rom[bank][address - 0x4000]
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
        }
    }

    override fun writeRom(address: Int, value: Int) {
        when(address) {
            // RAM Enable
            in 0x0000 until 0x2000 -> ramEnabled = (value and 0x0F) == 0b1010

            // ROM Bank Number lower 8 bits
            in 0x2000 until 0x3000 -> {
                currentRomBank = (currentRomBank and 0x100) or value
            }

            // ROM Bank Number upper bit
            in 0x3000 until 0x4000 -> {
                currentRomBank = setBit(currentRomBank, 8, value.getBit(0))
            }

            // RAM Bank Number
            in 0x4000 until 0x6000 -> {
                val newVal = value and 0x0F
                currentRamBank = newVal
            }
        }
    }

    override fun readRam(address: Int): Int {
        if (address !in 0xA000 until 0xC000) {
            throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
        }

        if (ram == null || !ramEnabled || currentRamBank >= ram.size) {
            return 0xFF
        }

        val newAddress = address - 0xA000
        return this.ram[currentRamBank][newAddress]
    }

    override fun writeRam(address: Int, value: Int) {
        if (address !in 0xA000 until 0xC000) {
            throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
        }


        if (ram == null || !ramEnabled || currentRamBank >= ram.size) {
            return
        }

        val newAddress = address - 0xA000
        val newVal = value and 0xFF
        this.ram[currentRamBank][newAddress] = newVal
    }

    override fun toString(): String {
        return "MBC5, ${rom.size} banks of ROM, ${ram?.size ?: 0} banks of RAM, Battery: $hasBattery"
    }
}