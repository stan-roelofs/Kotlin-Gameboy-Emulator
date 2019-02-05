package memory.cartridge

import memory.Memory
import utils.toHexString

interface CartridgeType : Memory {

    /** An array of RAM banks */
    val ram: Array<IntArray>?

    /** An array of ROM banks */
    val rom: Array<IntArray>

    override fun reset() {
        for (bank in rom) {
            bank.fill(0)
        }

        if (ram != null) {
            for (bank in ram!!) {
                bank.fill(0)
            }
        }
    }

    /** Loads cartridge ROM into this.rom */
    fun loadRom(value: ByteArray)

    /** Read ROM at location [address] */
    fun readRom(address: Int): Int

    /** Write [value] to ROM at location [address] */
    fun writeRom(address: Int, value: Int)

    /** Read RAM at location [address] */
    fun readRam(address: Int): Int

    /** Write [value] to RAM at location [address] */
    fun writeRam(address: Int, value: Int)

    override fun readByte(address: Int): Int {
        if (address in 0x0000 until 0x8000) {
            return readRom(address)
        }

        if (address in 0xA000 until 0xC000) {
            return readRam(address)
        }

        throw IllegalArgumentException("Address ${address.toHexString()} does not belong to cartridge")
    }

    override fun writeByte(address: Int, value: Int) {
        if (address in 0x0000 until 0x8000) {
            return writeRom(address, value)
        }

        if (address in 0xA000 until 0xC000) {
            return writeRam(address, value)
        }

        throw IllegalArgumentException("Address ${address.toHexString()} does not belong to cartridge")
    }
}