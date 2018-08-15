package memory

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

    private val hram = HRam()
    private val oam = Oam()
    private val internalRam = InternalRam()
    private val gpu = Gpu()
    var io = IO()

    var testOutput = false

    override fun reset() {
        io.reset()
        gpu.reset()
        internalRam.reset()
        oam.reset()
    }

    fun tick(cycles: Int) {
        io.tick(cycles)
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            in 0x0000 until 0x8000 -> cartridge!!.readByte(address)
            in 0x8000 until 0xA000 -> gpu.readByte(address)
            in 0xA000 until 0xC000 -> cartridge!!.readByte(address)
            in 0xC000 until 0xE000 -> internalRam.readByte(address)
            in 0xE000 until 0xFE00 -> internalRam.readByte(address)
            in 0xFE00 until 0xFEA0 -> oam.readByte(address)
            in 0xFEA0 until 0xFF00 -> return 0xFF
            in 0xFF00 until 0xFF4C -> io.readByte(address)
            in 0xFF4C until 0xFF80 -> return 0xFF
            in 0xFF80   ..  0xFFFF -> hram.readByte(address)
            else -> throw Exception("Error reading byte at address: ${Integer.toHexString(address)}")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        if (address == 0xFF02 && value == 0x81) {
            testOutput = true
        }

        val newVal = value and 0xFF
        when (address) {
            in 0x0000 until 0x8000 -> cartridge!!.writeByte(address, newVal)
            in 0x8000 until 0xA000 -> gpu.writeByte(address, newVal)
            in 0xA000 until 0xC000 -> cartridge!!.writeByte(address, newVal)
            in 0xC000 until 0xE000 -> internalRam.writeByte(address, newVal)
            in 0xE000 until 0xFE00 -> internalRam.writeByte(address, newVal)
            in 0xFE00 until 0xFEA0 -> oam.writeByte(address, newVal)
            in 0xFEA0 until 0xFF00 -> return
            in 0xFF00 until 0xFF4C -> io.writeByte(address, newVal)
            in 0xFF4C until 0xFF80 -> return
            in 0xFF80   ..  0xFFFF -> hram.writeByte(address, newVal)
            else -> throw Exception("Error writing byte at address: ${Integer.toHexString(address)}")
        }
    }
}
