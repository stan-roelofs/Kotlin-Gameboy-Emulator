package memory

import setSecondByte

interface Memory {

    /**
     * Resets each memory address to their default value
     */
    fun reset()

    /**
     * Reads an 8-bit value at [address]
     * @param address The address in memory that should be returned
     * @return The (unsigned) value of the byte at memory location [address]
     */
    fun readByte(address: Int): Int

    /**
     * Writes an 8-bit [value] to [address]
     * @param address The address in memory that should be returned
     * @param value The value that should be written to [address]
     */
    fun writeByte(address: Int, value: Int)

    /**
     * Reads a 16-bit value at [address]
     * @param address The address in memory that should be returned
     * @return The (unsigned) value of the word at memory location [address]
     */
    fun readWord(address: Int): Int {
        var value = readByte(address)
        value = setSecondByte(value, readByte(address + 1))
        return value
    }

    /**
     * Writes an 8-bit [value] to [address]
     * @param address The address in memory that should be returned
     * @param value The value that should be written to [address]
     */
    fun writeWord(address: Int, value: Int) {
        writeByte(address, value and 0xFF)
        writeByte(address + 1, value shr 8)
    }
}