package gameboy.memory

import gameboy.utils.toHexString

class HRam : Memory {

    private val hram: IntArray = IntArray(0x80)

    init {
        reset()
    }

    override fun reset() {
        hram.fill(0)
    }

    override fun readByte(address: Int): Int {
        if (address < 0xFF80 || address > 0xFFFF) {
            throw IllegalArgumentException("Address ${address.toHexString()} does not belong to HRAM")
        }

        return hram[address - 0xFF80]
    }

    override fun writeByte(address: Int, value: Int) {
        if (address < 0xFF80 || address > 0xFFFF) {
            throw IllegalArgumentException("Address ${address.toHexString()} does not belong to HRAM")
        }

        hram[address - 0xFF80] = value and 0xFF
    }
}