package gameboy.memory

class InternalRamDMG : InternalRam() {

    private val ram: IntArray = IntArray(0x2000)

    init {
        reset()
    }

    override fun reset() {
        ram.fill(0) //TODO: this should actually be random
    }

    override fun readByte(address: Int): Int {
        checkAddress(address)

        val addr = if (address >= 0xE000) address - 0xE000 else address - 0xC000
        return ram[addr]
    }

    override fun writeByte(address: Int, value: Int) {
        checkAddress(address)

        val addr = if (address >= 0xE000) address - 0xE000 else address - 0xC000
        ram[addr] = value and 0xFF
    }
}