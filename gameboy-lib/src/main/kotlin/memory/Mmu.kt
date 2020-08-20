package memory

import memory.cartridge.Cartridge
import memory.io.IO
import utils.Log
import utils.setBit
import utils.toHexString

class Mmu : Memory {

    companion object {
        // Constants

        // LCD
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

        // Interrupts
        const val IF = 0xFF0F
        const val IE = 0xFFFF

        // Timer
        const val DIV = 0xFF04
        const val TIMA = 0xFF05
        const val TMA = 0xFF06
        const val TAC = 0xFF07

        // Joypad
        const val P1 = 0xFF00

        // Serial
        const val SB = 0xFF01
        const val SC = 0xFF02

        // DMA
        const val DMA = 0xFF46

        // Sound
        const val NR10 = 0xFF10
        const val NR11 = 0xFF11
        const val NR12 = 0xFF12
        const val NR13 = 0xFF13
        const val NR14 = 0xFF14

        const val NR21 = 0xFF16
        const val NR22 = 0xFF17
        const val NR23 = 0xFF18
        const val NR24 = 0xFF19

        const val NR30 = 0xFF1A
        const val NR31 = 0xFF1B
        const val NR32 = 0xFF1C
        const val NR33 = 0xFF1D
        const val NR34 = 0xFF1E

        const val NR41 = 0xFF20
        const val NR42 = 0xFF21
        const val NR43 = 0xFF22
        const val NR44 = 0xFF23

        const val NR50 = 0xFF24
        const val NR51 = 0xFF25
        const val NR52 = 0xFF26
    }

    internal var cartridge: Cartridge? = null
    private val hram = HRam()
    private val oam = Oam()
    private val internalRam = InternalRam()
    val io = IO(this)

    init {
        reset()
    }

    override fun reset() {
        hram.reset()
        oam.reset()
        internalRam.reset()
        io.reset()
        cartridge?.reset()
    }

    internal fun tick(cycles: Int) {
        if (cycles != 4) {
            Log.w("Cycles != 4, should not be possible")
        }
        io.tick(cycles)
    }


    /**
     * Sets the bit at [pos] to true in the Interrupt Flags register
     */
    fun requestInterrupt(pos: Int) {
        var interruptFlags = readByte(IF)
        interruptFlags = setBit(interruptFlags, pos)
        writeByte(IF, interruptFlags)
    }

    override fun readByte(address: Int): Int {
        if (!io.dma.getOamAccessible()) {
            if (address in 0xFE00 until 0xFEA0) {
                return 0xFF
            }
        }

        return when(address) {
            in 0x0000 until 0x8000 -> cartridge!!.readByte(address)
            in 0x8000 until 0xA000 -> io.readByte(address)
            in 0xA000 until 0xC000 -> cartridge!!.readByte(address)
            in 0xC000 until 0xE000 -> internalRam.readByte(address)
            in 0xE000 until 0xFE00 -> internalRam.readByte(address)
            in 0xFE00 until 0xFEA0 -> oam.readByte(address)
            in 0xFEA0 until 0xFF00 -> return 0
            in 0xFF00 until 0xFF4C -> io.readByte(address)
            in 0xFF4C until 0xFF80 -> return 0xFF
            in 0xFF80   ..  0xFFFF -> hram.readByte(address)
            else -> throw Exception("Error reading byte at address: ${address.toHexString()}")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        if (!io.dma.getOamAccessible()) {
            if (address in 0xFE00 until 0xFEA0) {
                return
            }
        }

        val newVal = value and 0xFF
        when (address) {
            in 0x0000 until 0x8000 -> cartridge!!.writeByte(address, newVal)
            in 0x8000 until 0xA000 -> io.writeByte(address, newVal)
            in 0xA000 until 0xC000 -> cartridge!!.writeByte(address, newVal)
            in 0xC000 until 0xE000 -> internalRam.writeByte(address, newVal)
            in 0xE000 until 0xFE00 -> internalRam.writeByte(address, newVal)
            in 0xFE00 until 0xFEA0 -> {
                oam.writeByte(address, newVal)
                io.lcd.updateSprite(address, newVal)
            }
            in 0xFEA0 until 0xFF00 -> return
            in 0xFF00 until 0xFF4C -> io.writeByte(address, newVal)
            in 0xFF4C until 0xFF80 -> return
            in 0xFF80   ..  0xFFFF -> hram.writeByte(address, newVal)
            else -> throw Exception("Error writing byte at address: ${address.toHexString()}")
        }
    }

    /*
     * Used to read during DMA transfer, standard readByte prevents this as memory is not accessible
     * during DMA
     */
    internal fun dmaReadByte(address: Int): Int {
        return when(address) {
            in 0x0000 until 0x8000 -> cartridge!!.readByte(address)
            in 0x8000 until 0xA000 -> io.readByte(address)
            in 0xA000 until 0xC000 -> cartridge!!.readByte(address)
            in 0xC000 until 0xE000 -> internalRam.readByte(address)
            in 0xE000 until 0xFE00 -> internalRam.readByte(address)
            in 0xFE00 until 0xFEA0 -> oam.readByte(address)
            in 0xFEA0 until 0xFF00 -> return 0
            in 0xFF00 until 0xFF4C -> io.readByte(address)
            in 0xFF4C until 0xFF80 -> return 0xFF
            in 0xFF80   ..  0xFFFF -> hram.readByte(address)
            else -> throw Exception("Error reading byte at address: ${address.toHexString()}")
        }
    }

    /*
     * Used to write DMA transfer, standard writeByte prevents this as memory is not accessible
     * during DMA
     */
    internal fun dmaWriteByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when (address) {
            in 0xFE00 until 0xFEA0 -> {
                oam.writeByte(address, newVal)
                io.lcd.updateSprite(address, newVal)
            }
            else -> throw Exception("Error, DMA attempting to write to different address than OAM: ${address.toHexString()}")
        }
    }
}
