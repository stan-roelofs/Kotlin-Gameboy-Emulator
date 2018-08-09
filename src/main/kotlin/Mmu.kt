 class Mmu private constructor() {

     private object Holder {
         val INSTANCE = Mmu()
     }

    companion object {
        val instance: Mmu by lazy {Holder.INSTANCE}
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

     var testOutput = false

     fun reset() {
        io.fill(0)
        //io[0x04] = 0xAB
        io[0x05] = 0x00
        io[0x06] = 0x00
        io[0x07] = 0x00
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

    fun readByte(address: Int): Int {
        return when {
            address <= 0x7FFF -> {
                cartridge!!.readRom(address)
            }

            address in 0x8000..0x9FFF -> {
                vram[address - 0x8000]
            }

            address in 0xA000..0xBFFF -> {
               cartridge!!.readRam(address)
            }

            address in 0xC000..0xDFFF -> {
                wram[address - 0xC000]
            }

            address in 0xE000..0xFDFF -> {
                wram[address - 0x2000 - 0xC000]
            }

            address in 0xFE00..0xFE9F -> {
                oam[address - 0xFE00]
            }

            address in 0xFEA0..0xFEFF -> {
                0
            }

            address in 0xFF00..0xFF7F -> {
                io[address - 0xFF00]
            }

            address in 0xFF80..0xFFFE -> {
                hram[address - 0xFF80]
            }

            address == 0xFFFF -> {
                IE
            }

            else -> throw Exception("Error reading byte at address: ${Integer.toHexString(address)}")
        }
    }

    fun writeByte(address: Int, value: Int) {
        if (address == 0xFF02 && value == 0x81) {
            testOutput = true
        }
        when (address) {
            in 0x8000..0x9FFF -> {
                vram[address - 0x8000] = value and 0xFF
            }
            in 0xC000..0xDFFF -> {
                wram[address - 0xC000] = value and 0xFF
            }
            in 0xE000..0xFDFF -> {
                wram[address - 0x2000 - 0xC000] = value and 0xFF
            }
            in 0xFE00..0xFE9F -> {
                oam[address - 0xFE00] = value and 0xFF
            }
            in 0xFEA0..0xFEFF -> {
                // ignore write
            }
            in 0xFF00..0xFF7F -> {
                if (address == 0xFF04) {//todo
                    io[address - 0xFF00] = 0
                    return
                }
                io[address - 0xFF00] = value and 0xFF
            }
            in 0xFF80..0xFFFE -> {
                hram[address - 0xFF80] = value and 0xFF
            }
            0xFFFF -> {
                IE = value and 0xFF
            }
            else -> throw Exception("Error writing byte at address: ${Integer.toHexString(address)}")
        }
    }

    fun readWord(address: Int) : Int {
        var value = readByte(address)
        value = setSecondByte(value, readByte(address + 1))
        return value
    }

    fun writeWord(address: Int, value: Int) {
        writeByte(address, value and 0xFF)
        writeByte(address + 1, value shr 8)
    }
}

enum class MemoryType(val start: Int, val end: Int) {
    ROM_BANK_0                  (0x0000, 0x3FFF),
    ROM_BANK_SWITCHABLE         (0x4000, 0x7FFF),
    VIDEO_RAM                   (0x8000, 0x9FFF),
    RAM_BANK_SWITCHABLE         (0xA000, 0xBFFF),
    RAM_INTERNAL                (0xC000, 0xDFFF),
    RAM_INTERNAL_ECHO           (0xE000, 0xFDFF),
    SPRITE_ATTRIBUTE_MEMORY     (0xFE00, 0xFE9F),
    UNUSABLE_MEMORY_0           (0xFEA0, 0xFEFF),
    IO_PORTS                    (0xFF00, 0xFF4B),
    UNUSABLE_MEMORY_1           (0xFF4C, 0xFF7F),
    INTERRUPT_ENABLE_REGISTER   (0xFFFF, 0xFFFF)
}

