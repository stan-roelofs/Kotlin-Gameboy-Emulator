package gameboy.memory.io.graphics

import gameboy.utils.toHexString

class Vram(numBanks: Int) {

    private val vram = Array(numBanks) {IntArray(0x2000)}

    init {
        reset()
    }

    fun reset() {
        for (bank in vram)
            bank.fill(0)
    }

    fun readByte(bank: Int, address: Int): Int {
        if (address < 0x8000 || address >= 0xA000) {
            throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Vram")
        }

        return vram[bank][address - 0x8000]
    }

    fun writeByte(bank: Int, address: Int, value: Int) {
        if (address < 0x8000 || address >= 0xA000) {
            throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Vram")
        }

        vram[bank][address - 0x8000] = value and 0xFF
    }
}