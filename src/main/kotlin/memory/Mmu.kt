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

    var testOutput = false

    override fun reset() {
        io.fill(0)
        //io[0x04] = 0xAB
        timer.reset()
        
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
        io[0x40] = 0x91
        io[0x42] = 0x00
        io[0x43] = 0x00
        io[0x45] = 0x00
        io[0x47] = 0xFC
        io[0x48] = 0xFF
        io[0x49] = 0xFF
        io[0x4A] = 0x00
        io[0x4B] = 0x00

        IE = 0x00
    }

    fun tick(cycles: Int) {
        timer.tick(cycles)
    }

    override fun readByte(address: Int): Int {
        return when {
            address <= 0x7FFF -> cartridge!!.readRom(address)
            address in 0x8000..0x9FFF -> vram[address - 0x8000]
            address in 0xA000..0xBFFF -> cartridge!!.readRam(address)
            address in 0xC000..0xDFFF -> wram[address - 0xC000]
            address in 0xE000..0xFDFF -> wram[address - 0x2000 - 0xC000]
            address in 0xFE00..0xFE9F -> oam[address - 0xFE00]
            address in 0xFEA0..0xFEFF -> 0
            address in 0xFF00..0xFF7F -> {
                when(address) { //TODO
                    DIV, TIMA, TMA, TAC -> timer.readByte(address)
                    else -> io[address - 0xFF00]
                }
            }
            address in 0xFF80..0xFFFE -> hram[address - 0xFF80]
            address == 0xFFFF -> IE
            else -> throw Exception("Error reading byte at address: ${Integer.toHexString(address)}")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        if (address == 0xFF02 && value == 0x81) {
            testOutput = true
        }
        when (address) {
            in 0x8000..0x9FFF -> vram[address - 0x8000] = value and 0xFF
            in 0xC000..0xDFFF -> wram[address - 0xC000] = value and 0xFF
            in 0xE000..0xFDFF -> wram[address - 0x2000 - 0xC000] = value and 0xFF
            in 0xFE00..0xFE9F -> oam[address - 0xFE00] = value and 0xFF
            in 0xFEA0..0xFEFF -> return
            in 0xFF00..0xFF7F -> {
                when(address) { //TODO
                    DIV, TIMA, TMA, TAC -> timer.writeByte(address, value)
                    else -> io[address - 0xFF00] = value and 0xFF
                }
            }
            in 0xFF80..0xFFFE -> hram[address - 0xFF80] = value and 0xFF
            0xFFFF -> IE = value and 0xFF
            else -> throw Exception("Error writing byte at address: ${Integer.toHexString(address)}")
        }
    }
}
