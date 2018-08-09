package memory

import Cartridge
import setSecondByte

class Mmu private constructor() : Memory {


    private object Holder {
        val INSTANCE = Mmu()
    }

    companion object {
        val instance: Mmu by lazy { Holder.INSTANCE }

        // Constants
        const val SVBK = 0xFF80
        const val VBK = 0xFF4F
        const val LCDC = 0xFF40
        const val LY = 0xFF44
        const val LYC = 0xFF45
        const val STAT = 0xFF41
        const val SCY = 0xFF42
        const val SCX = 0xFF43
        const val WY = 0xFF4A
        const val WX = 0xFF4B
        const val BGP = 0xFF47
        const val OBP0 = 0xFF48
        const val OBP1 = 0xFF49

        const val IF = 0xFF0F
        const val IE = 0xFFFF

        const val DIV = 0xFF04
        const val TIMA = 0xFF05
        const val TMA = 0xFF06
        const val TAC = 0xFF07

        const val P1 = 0xFF00

        const val DMA = 0xFF46
    }

    var cartridge: Cartridge? = null
    set(value) {
        field = value
        reset()
    }

    val io: IntArray = IntArray(0x80)
    private val vram: IntArray = IntArray(0x2000)
    private val wram: IntArray = IntArray(0x2000)
    private val oam: IntArray = IntArray(0xA0)
    private val hram: IntArray = IntArray(0x7F)
    //private val wramx: IntArray

    private var IE: Int = 0x00

    private val timer = Timer()
    private val lcd = Lcd()

    var testOutput = false

    override fun reset() {
        io.fill(0)
        //io[0x04] = 0xAB
        timer.reset()
        lcd.reset()

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
        io[0x26] = if (cartridge!!.isSgb) 0xF0 else 0xF1

        IE = 0x00
    }

    fun tick(cycles: Int) {
        timer.tick(cycles)
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            in 0x0000 until 0x8000 -> return cartridge!!.readRom(address)
            in 0x8000 until 0xA000 -> vram[address - 0x8000]
            in 0xA000 until 0xC000 -> cartridge!!.readRam(address)
            in 0xC000 until 0xE000 -> wram[address - 0xC000]
            in 0xE000 until 0xFE00 -> wram[address - 0xE000]
            in 0xFE00 until 0xFEA0 -> oam[address - 0xFE00]
            in 0xFEA0 until 0xFF00 -> return 0
            in 0xFF00 until 0xFF4C -> {
                when(address) {
                    DIV, TIMA, TMA, TAC -> timer.readByte(address)
                    LCDC, LY, LYC, STAT, SCY, SCX, WY, WX, BGP, OBP0, OBP1 -> lcd.readByte(address)
                    else -> io[address - 0xFF00]
                }
            }
            in 0xFF4C until 0xFF80 -> return 0
            in 0xFF80 until 0xFFFF -> hram[address - 0xFF80]
            0xFFFF -> IE
            else -> throw Exception("Error reading byte at address: ${Integer.toHexString(address)}")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        if (address == 0xFF02 && value == 0x81) {
            testOutput = true
        }

        val newVal = value and 0xFF
        when (address) {
            in 0x0000 until 0x8000 -> return //TODO write cartridge
            in 0x8000 until 0xA000 -> vram[address - 0x8000] = newVal
            in 0xA000 until 0xC000 -> cartridge!!.writeRam(address, newVal)
            in 0xC000 until 0xE000 -> wram[address - 0xC000] = newVal
            in 0xE000 until 0xFE00 -> wram[address - 0xE000] = newVal
            in 0xFE00 until 0xFEA0 -> oam[address - 0xFE00] = newVal
            in 0xFEA0 until 0xFF00 -> return
            in 0xFF00 until 0xFF4C -> {
                when(address) {
                    DIV, TIMA, TMA, TAC -> timer.writeByte(address, newVal)
                    LCDC, LY, LYC, STAT, SCY, SCX, WY, WX, BGP, OBP0, OBP1 -> lcd.writeByte(address, newVal)
                    else -> io[address - 0xFF00] = newVal
                }
            }
            in 0xFF4C until 0xFF80 -> return
            in 0xFF80 until 0xFFFF -> hram[address - 0xFF80] = newVal
            0xFFFF -> IE = newVal
            else -> throw Exception("Error writing byte at address: ${Integer.toHexString(address)}")
        }
    }
}
