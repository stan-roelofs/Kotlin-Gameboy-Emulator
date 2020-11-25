package gameboy.memory.io

import gameboy.memory.Mmu
import gameboy.memory.io.graphics.PpuCGB
import gameboy.memory.io.sound.Sound

class IOCGB(mmu : Mmu) : IO(mmu) {

    override val ppu = PpuCGB(mmu)
    override val sound = Sound()
    override val dma = Dma(mmu)
    override val timer = Timer(mmu)
    private val hdma = Hdma(mmu)

    init {
        reset()
    }

    override fun tick(cycles: Int) {
        super.tick(cycles)
        //hdma.tick(cycles, ppu.getMode() == Ppu.ModeEnum.HBLANK, ppu.lcdEnabled())
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            // Joypad
            Mmu.P1 -> joypad.readByte(address)

            // Serial
            Mmu.SB,
            Mmu.SC -> serial.readByte(address)

            // Sound
            Mmu.NR10,
            Mmu.NR11,
            Mmu.NR12,
            Mmu.NR13,
            Mmu.NR14,
            Mmu.NR21,
            Mmu.NR22,
            Mmu.NR23,
            Mmu.NR24,
            Mmu.NR30,
            Mmu.NR31,
            Mmu.NR32,
            Mmu.NR33,
            Mmu.NR34,
            Mmu.NR41,
            Mmu.NR42,
            Mmu.NR43,
            Mmu.NR44,
            Mmu.NR50,
            Mmu.NR51,
            Mmu.NR52,
            in 0xFF30..0xFF3F -> sound.readByte(address)

            // Timer
            Mmu.DIV,
            Mmu.TIMA,
            Mmu.TMA,
            Mmu.TAC -> timer.readByte(address)

            // IF
            Mmu.IF -> this.IF or 0b11100000

            // Dma
            Mmu.DMA -> dma.readByte(address)

            // Hdma
            Mmu.HDMA1,
            Mmu.HDMA2,
            Mmu.HDMA3,
            Mmu.HDMA4,
            Mmu.HDMA5 -> hdma.readByte(address)

            // Lcd
            Mmu.LCDC,
            Mmu.LY,
            Mmu.LYC,
            Mmu.STAT,
            Mmu.SCY,
            Mmu.SCX,
            Mmu.WY,
            Mmu.WX,
            Mmu.BGP,
            Mmu.OBP0,
            Mmu.OBP1,
            Mmu.VBK,
            Mmu.BCPS,
            Mmu.BCPD,
            Mmu.OCPS,
            Mmu.OCPD,
            in 0x8000 until 0xA000 -> ppu.readByte(address)
            else -> 0xFF
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF

        when(address) {
            // Joypad
            Mmu.P1 -> joypad.writeByte(address, newVal)

            // Serial
            Mmu.SB,
            Mmu.SC -> serial.writeByte(address, newVal)

            // Sound
            Mmu.NR10,
            Mmu.NR11,
            Mmu.NR12,
            Mmu.NR13,
            Mmu.NR14,
            Mmu.NR21,
            Mmu.NR22,
            Mmu.NR23,
            Mmu.NR24,
            Mmu.NR30,
            Mmu.NR31,
            Mmu.NR32,
            Mmu.NR33,
            Mmu.NR34,
            Mmu.NR41,
            Mmu.NR42,
            Mmu.NR43,
            Mmu.NR44,
            Mmu.NR50,
            Mmu.NR51,
            Mmu.NR52,
            in 0xFF30..0xFF3F -> sound.writeByte(address, newVal)

            // Timer
            Mmu.DIV,
            Mmu.TIMA,
            Mmu.TMA,
            Mmu.TAC -> timer.writeByte(address, newVal)

            // IF
            Mmu.IF -> this.IF = newVal

            // Dma
            Mmu.DMA -> dma.writeByte(address, newVal)

            // Hdma
            Mmu.HDMA1,
            Mmu.HDMA2,
            Mmu.HDMA3,
            Mmu.HDMA4,
            Mmu.HDMA5 -> hdma.writeByte(address, newVal)

            // Lcd
            Mmu.LCDC,
            Mmu.LY,
            Mmu.LYC,
            Mmu.STAT,
            Mmu.SCY,
            Mmu.SCX,
            Mmu.WY,
            Mmu.WX,
            Mmu.BGP,
            Mmu.OBP0,
            Mmu.OBP1,
            Mmu.VBK,
            Mmu.BCPS,
            Mmu.BCPD,
            Mmu.OCPS,
            Mmu.OCPD,
            in 0x8000 until 0xA000 -> ppu.writeByte(address, newVal)
            else -> return
        }
    }
}