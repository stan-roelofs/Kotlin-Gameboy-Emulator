package gameboy.memory

import gameboy.memory.cartridge.Cartridge
import gameboy.memory.io.IOCGB
import gameboy.utils.toHexString

class MmuCGB(cartridge: Cartridge) : Mmu(cartridge) {

    override val hram = HRam()
    override val internalRam = InternalRamCGB()
    override val oam = Oam()
    override val io = IOCGB(this)

    init {
        reset()
    }

    override fun readByte(address: Int): Int {
        if (!io.dma.getOamAccessible()) {
            if (address in 0xFE00 until 0xFEA0) {
                return 0xFF
            }
        }

        return when(address) {
            in 0x0000 until 0x8000 -> cartridge.readByte(address)
            in 0x8000 until 0xA000 -> io.readByte(address)
            in 0xA000 until 0xC000 -> cartridge.readByte(address)
            in 0xC000 until 0xE000 -> internalRam.readByte(address)
            in 0xE000 until 0xFE00 -> internalRam.readByte(address)
            in 0xFE00 until 0xFEA0 -> oam.readByte(address)
            in 0xFEA0 until 0xFF00 -> return 0 // TODO: emulate this for gbc
            in 0xFF00 until 0xFF4C -> io.readByte(address)
            in 0xFF4C until 0xFF4F -> return 0xFF
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
            in 0x0000 until 0x8000 -> cartridge.writeByte(address, newVal)
            in 0x8000 until 0xA000 -> io.writeByte(address, newVal)
            in 0xA000 until 0xC000 -> cartridge.writeByte(address, newVal)
            in 0xC000 until 0xE000 -> internalRam.writeByte(address, newVal)
            in 0xE000 until 0xFE00 -> internalRam.writeByte(address, newVal)
            in 0xFE00 until 0xFEA0 -> oam.writeByte(address, newVal)
            in 0xFEA0 until 0xFF00 -> return // TODO: emulate this for gbc
            in 0xFF00 until 0xFF4C -> io.writeByte(address, newVal)
            in 0xFF4C until 0xFF4F -> return
            VBK -> io.writeByte(address, newVal) // VRAM bank
            in 0xFF50 until 0xFF68 -> return
            BCPS,
            BCPD,
            OCPS,
            OCPD -> io.writeByte(address, newVal)
            in 0xFF6C until 0xFF70 -> return
            SVBK -> internalRam.writeByte(address, newVal) // WRAM bank
            in 0xFF71 until 0xFF80 -> return
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
            in 0xFEA0 until 0xFF00 -> return 0 // TODO: emulate this for gbc
            in 0xFF00 until 0xFF4C -> io.readByte(address)
            in 0xFF4C until 0xFF4F -> return 0xFF
            VBK -> io.readByte(address) // VRAM bank
            in 0xFF50 until 0xFF68 -> return 0xFF
            BCPS,
            BCPD,
            OCPS,
            OCPD -> io.readByte(address)
            in 0xFF6C until 0xFF70 -> return 0xFF
            SVBK -> internalRam.readByte(address) // WRAM bank
            in 0xFF71 until 0xFF80 -> return 0xFF
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
            in 0xFE00 until 0xFEA0 -> oam.writeByte(address, newVal)
            else -> throw Exception("Error, DMA attempting to write to different address than OAM: ${address.toHexString()}")
        }
    }
}