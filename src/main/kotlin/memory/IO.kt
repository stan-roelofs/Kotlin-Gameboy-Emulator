package memory

import utils.setSecondByte

class IO : Memory {

    // These are public such that gui can read the LCD data to render,
    // and can send key presses/releases to the Joypad
    val lcd = Lcd()
    val joypad = Joypad()

    private val timer = Timer()
    private val io = IntArray(0xFF)

    override fun reset() {
        io.fill(0)

        timer.reset()
        lcd.reset()
        joypad.reset()

        io[0x10] = 0x80
        io[0x11] = 0xBF
        io[0x12] = 0xF3
        io[0x14] = 0xBF
        io[0x16] = 0x3F
        io[0x17] = 0x00
        io[0x19] = 0xBF
        io[0x1A] = 0x7F
        io[0x1B] = 0xFF
        io[0x1C] = 0x9F
        io[0x1E] = 0xBF
        io[0x20] = 0xFF
        io[0x21] = 0x00
        io[0x22] = 0x00
        io[0x23] = 0xBF
        io[0x24] = 0x77
        io[0x25] = 0xF3
        io[0x26] = 0xF1

        io[0x0F] = 0xE1
    }

    fun tick(cycles: Int) {
        timer.tick(cycles)
        lcd.tick(cycles)
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.P1 -> joypad.readByte(address)
            Mmu.DIV, Mmu.TIMA, Mmu.TMA, Mmu.TAC -> timer.readByte(address)
            Mmu.LCDC, Mmu.LY, Mmu.LYC, Mmu.STAT, Mmu.SCY, Mmu.SCX, Mmu.WY, Mmu.WX, Mmu.BGP, Mmu.OBP0, Mmu.OBP1 -> lcd.readByte(address)
            else -> io[address - 0xFF00]
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF

        when(address) {
            0xFF46 -> DmaTransfer(newVal)
            Mmu.P1 -> joypad.writeByte(address, newVal)
            Mmu.DIV, Mmu.TIMA, Mmu.TMA, Mmu.TAC -> timer.writeByte(address, newVal)
            Mmu.LCDC, Mmu.LY, Mmu.LYC, Mmu.STAT, Mmu.SCY, Mmu.SCX, Mmu.WY, Mmu.WX, Mmu.BGP, Mmu.OBP0, Mmu.OBP1 -> lcd.writeByte(address, newVal)
            else -> io[address - 0xFF00] = newVal
        }
    }

    // TODO: implement this properly
    private fun DmaTransfer(start: Int) {
        val mmu = Mmu.instance
        var source = 0
        source = setSecondByte(source, start)

        for (i in 0 until 160) {
            mmu.writeByte(0xFE00 + i, mmu.readByte(source + i))
        }
    }
}