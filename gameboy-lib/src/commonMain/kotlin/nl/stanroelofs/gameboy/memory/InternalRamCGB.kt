package nl.stanroelofs.gameboy.memory

class InternalRamCGB : InternalRam() {

    private var currentBank = 0
    private val ram = Array(8){IntArray(0x1000)}

    init {
        reset()
    }

    override fun reset() {
        for (bank in ram) {
            bank.fill(0) // TODO; this should be random
        }
        currentBank = 0
    }

    override fun readByte(address: Int): Int {
        if (address == Mmu.SVBK) {
            return currentBank or 0b11111000
        }

        checkAddress(address)

        val addr = if (address >= 0xE000) address - 0xE000 else address - 0xC000
        val bank = if (currentBank == 0) 1 else currentBank
        return if (addr < 0x1000) ram[0][addr] else ram[bank][addr - 0x1000]
    }

    override fun writeByte(address: Int, value: Int) {
        if (address == Mmu.SVBK) {
            currentBank = value and 0b00000111
            return
        }

        checkAddress(address)

        val addr = if (address >= 0xE000) address - 0xE000 else address - 0xC000
        val bank = if (currentBank == 0) 1 else currentBank
        if (addr < 0x1000)
            ram[0][addr] = value and 0xFF
        else
            ram[bank][addr - 0x1000] = value and 0xFF
    }

}