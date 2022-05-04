package nl.stanroelofs.gameboy.memory.cartridge

import nl.stanroelofs.gameboy.memory.Memory
import nl.stanroelofs.gameboy.utils.toHexString
import java.io.InputStream
import java.io.OutputStream
import kotlin.math.roundToInt

interface CartridgeType : Memory {

    /** An array of RAM banks */
    val ram: Array<IntArray>?

    /** An array of ROM banks */
    val rom: Array<IntArray>

    /** Whether the cartridge has a battery */
    val hasBattery: Boolean

    override fun reset() {
        if (ram != null) {
            for (bank in ram!!) {
                bank.fill((Math.random() * Byte.MAX_VALUE).roundToInt() and 0xFF)
            }
        }
    }

    /** Loads cartridge ROM*/
    fun loadRom(value: ByteArray)

    /** Read ROM at location [address] */
    fun readRom(address: Int): Int

    /** Write [value] to ROM at location [address] */
    fun writeRom(address: Int, value: Int)

    /** Read RAM at location [address] */
    fun readRam(address: Int): Int

    /** Write [value] to RAM at location [address] */
    fun writeRam(address: Int, value: Int)

    /** Write current state of RAM to [destination] */
    fun saveRam(destination: OutputStream) {
        throw IllegalStateException("This cartridge type cannot load/save")
    }

    /** Loads RAM state from [source] */
    fun loadRam(source: InputStream) {
        throw IllegalStateException("This cartridge type cannot load/save")
    }

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

    override fun toString(): String
}