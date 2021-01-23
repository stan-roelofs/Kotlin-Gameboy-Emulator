package gameboy.memory.io

import gameboy.memory.Memory
import gameboy.memory.Mmu
import gameboy.memory.io.graphics.Ppu
import gameboy.memory.io.graphics.PpuCGB
import gameboy.memory.io.graphics.PpuDMG
import gameboy.memory.io.sound.Sound

abstract class IO(mmu: Mmu) : Memory {

    // These are public such that gui can read the LCD data to render,
    // and can send key presses/releases to the Joypad
    abstract val ppu: Ppu
    val joypad = Joypad(mmu)
    val serial = Serial()
    val sound = Sound()
    val dma = Dma(mmu)
    val timer = Timer(mmu)

    private var IF = 0

    override fun reset() {
        timer.reset()
        ppu.reset()
        joypad.reset()
        dma.reset()
        serial.reset()
        sound.reset()

        IF = 0x1
    }

    open fun tick(cycles: Int) {
        timer.tick(if (cycles == 2) 1 else 2)
        ppu.tick(cycles)
        dma.tick(cycles)
        sound.tick(cycles)
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
            in 0x8000 until 0xA000 -> ppu.readByte(address)
            else -> 0xFF
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF

        when (address) {
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
            in 0x8000 until 0xA000 -> ppu.writeByte(address, newVal)
            else -> return
        }
    }
}

class IOCGB(mmu : Mmu) : IO(mmu) {

    override val ppu = PpuCGB(mmu)
    val hdma = Hdma(mmu)

    init {
        reset()
    }

    override fun reset() {
        hdma.reset()
    }

    override fun tick(cycles: Int) {
        super.tick(cycles)
        hdma.tick(cycles, ppu.getMode() == Ppu.ModeEnum.HBLANK, ppu.lcdc.getLcdEnable())
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            // Hdma
            Mmu.HDMA1,
            Mmu.HDMA2,
            Mmu.HDMA3,
            Mmu.HDMA4,
            Mmu.HDMA5 -> hdma.readByte(address)

            // Lcd
            Mmu.VBK,
            Mmu.BCPS,
            Mmu.BCPD,
            Mmu.OCPS,
            Mmu.OCPD -> ppu.readByte(address)
            else -> super.readByte(address)
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF

        when(address) {
            // Hdma
            Mmu.HDMA1,
            Mmu.HDMA2,
            Mmu.HDMA3,
            Mmu.HDMA4,
            Mmu.HDMA5 -> hdma.writeByte(address, newVal)

            // Lcd
            Mmu.VBK,
            Mmu.BCPS,
            Mmu.BCPD,
            Mmu.OCPS,
            Mmu.OCPD -> ppu.writeByte(address, newVal)
            else -> super.writeByte(address, newVal)
        }
    }
}

class IODMG(mmu : Mmu) : IO(mmu) {
    override val ppu = PpuDMG(mmu)

    init {
        reset()
    }
}