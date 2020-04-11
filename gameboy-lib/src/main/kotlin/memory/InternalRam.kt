package memory

import utils.toHexString

class InternalRam : Memory {

    private val ram: IntArray = IntArray(0x2000)

    init {
        reset()
    }

    override fun reset() {
        ram.fill(0) //TODO: this should actually be random
    }

    override fun readByte(address: Int): Int {
        if (address < 0xC000 || address >= 0xFE00) {
            throw IllegalArgumentException("Address ${address.toHexString()} does not belong to InternalRam")
        }

        val addr = if (address >= 0xE000) address - 0xE000 else address - 0xC000
        return ram[addr]
    }

    override fun writeByte(address: Int, value: Int) {
        if (address < 0xC000 || address >= 0xFE00) {
            throw IllegalArgumentException("Address ${address.toHexString()} does not belong to InternalRam")
        }

        val addr = if (address >= 0xE000) address - 0xE000 else address - 0xC000
        ram[addr] = value and 0xFF
    }
}