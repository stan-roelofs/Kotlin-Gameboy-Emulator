package memory

import utils.toHexString

class Oam : Memory {

    private val oam: IntArray = IntArray(0xA0)

    init {
        reset()
    }

    override fun reset() {
        oam.fill(0)
    }

    override fun readByte(address: Int): Int {
        if (address < 0xFE00 || address >= 0xFEA0) {
            throw IllegalArgumentException("Address ${address.toHexString()} does not belong to OAM")
        }

        return oam[address - 0xFE00]
    }

    override fun writeByte(address: Int, value: Int) {
        if (address < 0xFE00 || address >= 0xFEA0) {
            throw IllegalArgumentException("Address ${address.toHexString()} does not belong to OAM")
        }

        oam[address - 0xFE00] = value and 0xFF
    }
}