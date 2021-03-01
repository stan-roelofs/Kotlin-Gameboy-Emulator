package nl.stanroelofs.gameboy.memory.cartridge

import nl.stanroelofs.gameboy.memory.Memory
import nl.stanroelofs.gameboy.utils.toHexString

class ROMONLY : Memory, CartridgeType {
    override val ram: Array<IntArray>? = arrayOf(intArrayOf(0))
    override val rom: Array<IntArray> = arrayOf(IntArray(0x8000))

    override fun loadRom(value: ByteArray) {
        for (i in 0 until value.size) {
            rom[0][i] = (value[i].toInt()) and 0xFF
        }
    }

    override fun readRom(address: Int): Int {
        if (address in 0x0000 until 0x8000) {
            return this.rom[0][address]
        }

        throw IllegalArgumentException("Address ${address.toHexString(2)} out of bounds")
    }

    override fun writeRom(address: Int, value: Int) {
        return
    }

    override fun readRam(address: Int): Int {
        return 0xFF
    }

    override fun writeRam(address: Int, value: Int) {
        return
    }

    override fun hasBattery(): Boolean {
        return false
    }

    override fun toString(): String {
        return "ROM Only"
    }
}