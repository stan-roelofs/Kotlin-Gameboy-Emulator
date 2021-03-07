package nl.stanroelofs.gameboy.memory

import nl.stanroelofs.gameboy.cpu.Interrupt
import nl.stanroelofs.gameboy.memory.cartridge.Cartridge
import nl.stanroelofs.gameboy.memory.io.IO
import nl.stanroelofs.gameboy.memory.io.IOCGB
import nl.stanroelofs.gameboy.memory.io.IODMG
import nl.stanroelofs.gameboy.utils.setBit
import nl.stanroelofs.gameboy.utils.toHexString

abstract class Mmu(val cartridge: Cartridge) : Memory {

    companion object {
        // LCD
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
        const val BCPS = 0xFF68
        const val BCPD = 0xFF69
        const val OCPS = 0xFF6A
        const val OCPD = 0xFF6B

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
        const val HDMA1 = 0xFF51
        const val HDMA2 = 0xFF52
        const val HDMA3 = 0xFF53
        const val HDMA4 = 0xFF54
        const val HDMA5 = 0xFF55

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

        // CGB
        const val KEY1 = 0xFF4D
        const val RP = 0xFF56
        const val OPRI = 0xFF6C
        const val SVBK = 0xFF70
    }

    internal abstract val hram : HRam
    internal abstract val internalRam : InternalRam
    internal abstract val oam : Oam
    abstract val io : IO

    final override fun reset() {
        hram.reset()
        oam.reset()
        internalRam.reset()
        io.reset()
        cartridge.reset()
    }

    internal fun tick(cycles: Int) {
        if (cycles !in 1..2)
            throw IllegalArgumentException("cycles must be 1 or 2")

        io.tick(cycles)
    }

    /**
     * Sets the bit at [pos] to true in the Interrupt Flags register
     */
    fun requestInterrupt(pos: Interrupt) {
        var interruptFlags = readByte(IF)
        interruptFlags = interruptFlags.setBit(pos.ordinal)
        writeByte(IF, interruptFlags)
    }

    /*
     * Used to read during DMA transfer, standard readByte prevents this as memory is not accessible
     * during DMA
     */
    internal abstract fun dmaReadByte(address: Int): Int

    /*
     * Used to write DMA transfer, standard writeByte prevents this as memory is not accessible
     * during DMA
     */
    internal abstract fun dmaWriteByte(address: Int, value: Int)
}

class MmuCGB(cartridge: Cartridge) : Mmu(cartridge) {

    override val hram = HRam()
    override val internalRam = InternalRamCGB()
    override val oam = Oam()
    override val io = IOCGB(this)
    lateinit var key1: Register

    init {
        reset()
    }

    override fun readByte(address: Int): Int {
        if (!io.dma.getOamAccessible()) {
            if (address in 0xFE00 until 0xFEA0) {
                return 0xFF
            }
        }

        return dmaReadByte(address)
    }

    override fun writeByte(address: Int, value: Int) {
        if (!io.dma.getOamAccessible()) {
            if (address in 0xFE00 until 0xFEA0) {
                return
            }
        }

        val newVal = value and 0xFF
        when (address) {
            in 0x0000 until 0x8000 -> cartridge.writeByte(address, newVal)
            in 0x8000 until 0xA000 -> io.writeByte(address, newVal)
            in 0xA000 until 0xC000 -> cartridge.writeByte(address, newVal)
            in 0xC000 until 0xE000 -> internalRam.writeByte(address, newVal)
            in 0xE000 until 0xFE00 -> internalRam.writeByte(address, newVal)
            in 0xFE00 until 0xFEA0 -> oam.writeByte(address, newVal)
            in 0xFEA0 until 0xFF00 -> return // TODO: emulate this for gbc
            in 0xFF00 until 0xFF4C -> io.writeByte(address, newVal)
            in 0xFF4C until 0xFF4D -> return
            KEY1 -> key1.value = key1.value or (newVal and 0b1) // Prepare mode switch
            in 0xFF4E until 0xFF4F -> return
            VBK -> io.writeByte(address, newVal) // VRAM bank
            0xFF50 -> return
            HDMA1,
            HDMA2,
            HDMA3,
            HDMA4,
            HDMA5 -> io.writeByte(address, newVal)
            in 0xFF56 until 0xFF68 -> return
            BCPS,
            BCPD,
            OCPS,
            OCPD -> io.writeByte(address, newVal)
            in 0xFF6C until 0xFF70 -> return
            SVBK -> internalRam.writeByte(address, newVal) // WRAM bank
            in 0xFF71 until 0xFF80 -> return
            in 0xFF80..0xFFFF -> hram.writeByte(address, newVal)
            else -> throw Exception("Error writing byte at address: ${address.toHexString()}")
        }
    }

    /*
     * Used to read during DMA transfer, standard readByte prevents this as memory is not accessible
     * during DMA
     */
    override fun dmaReadByte(address: Int): Int {
        return when (address) {
            in 0x0000 until 0x8000 -> cartridge.readByte(address)
            in 0x8000 until 0xA000 -> io.readByte(address)
            in 0xA000 until 0xC000 -> cartridge.readByte(address)
            in 0xC000 until 0xE000 -> internalRam.readByte(address)
            in 0xE000 until 0xFE00 -> internalRam.readByte(address)
            in 0xFE00 until 0xFEA0 -> oam.readByte(address)
            in 0xFEA0 until 0xFF00 -> 0 // TODO: emulate this for gbc
            in 0xFF00 until 0xFF4C -> io.readByte(address)
            in 0xFF4C until 0xFF4D -> 0xFF
            KEY1 -> key1.value or 0b01111110
            in 0xFF4E until 0xFF4F -> 0xFF
            VBK -> io.readByte(address) // VRAM bank
            0xFF50 -> return 0xFF
            HDMA1,
            HDMA2,
            HDMA3,
            HDMA4,
            HDMA5 -> io.readByte(address)
            in 0xFF56 until 0xFF68 -> return 0xFF
            BCPS,
            BCPD,
            OCPS,
            OCPD -> io.readByte(address)
            in 0xFF6C until 0xFF70 -> return 0xFF
            SVBK -> internalRam.readByte(address) // WRAM bank
            in 0xFF71 until 0xFF80 -> return 0xFF
            in 0xFF80..0xFFFF -> hram.readByte(address)
            else -> throw Exception("Error reading byte at address: ${address.toHexString()}")
        }
    }

    /*
     * Used to write DMA transfer, standard writeByte prevents this as memory is not accessible
     * during DMA
     */
    override fun dmaWriteByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when (address) {
            in 0xFE00 until 0xFEA0 -> oam.writeByte(address, newVal)
            else -> throw Exception("Error, DMA attempting to write to different address than OAM: ${address.toHexString()}")
        }
    }
}

class MmuDMG(cartridge: Cartridge) : Mmu(cartridge) {

    override val hram = HRam()
    override val oam = Oam()
    override val internalRam = InternalRamDMG()
    override val io = IODMG(this)

    init {
        reset()
    }

    override fun readByte(address: Int): Int {
        if (!io.dma.getOamAccessible()) {
            if (address in 0xFE00 until 0xFEA0) {
                return 0xFF
            }
        }

        return dmaReadByte(address)
    }

    override fun writeByte(address: Int, value: Int) {
        if (!io.dma.getOamAccessible()) {
            if (address in 0xFE00 until 0xFEA0) {
                return
            }
        }

        val newVal = value and 0xFF
        when (address) {
            in 0x0000 until 0x8000 -> cartridge.writeByte(address, newVal)
            in 0x8000 until 0xA000 -> io.writeByte(address, newVal)
            in 0xA000 until 0xC000 -> cartridge.writeByte(address, newVal)
            in 0xC000 until 0xE000 -> internalRam.writeByte(address, newVal)
            in 0xE000 until 0xFE00 -> internalRam.writeByte(address, newVal)
            in 0xFE00 until 0xFEA0 -> oam.writeByte(address, newVal)
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
    override fun dmaReadByte(address: Int): Int {
        return when(address) {
            in 0x0000 until 0x8000 -> cartridge.readByte(address)
            in 0x8000 until 0xA000 -> io.readByte(address)
            in 0xA000 until 0xC000 -> cartridge.readByte(address)
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
    override fun dmaWriteByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when (address) {
            in 0xFE00 until 0xFEA0 -> {
                oam.writeByte(address, newVal)
            }
            else -> throw Exception("Error, DMA attempting to write to different address than OAM: ${address.toHexString()}")
        }
    }
}
