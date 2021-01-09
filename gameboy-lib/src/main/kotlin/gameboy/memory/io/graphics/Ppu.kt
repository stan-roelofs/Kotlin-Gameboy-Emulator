package gameboy.memory.io.graphics

import gameboy.cpu.Interrupt
import gameboy.memory.Memory
import gameboy.memory.Mmu
import gameboy.memory.Register
import gameboy.memory.io.graphics.mode.Hblank
import gameboy.memory.io.graphics.mode.OamSearch
import gameboy.memory.io.graphics.mode.PixelTransfer
import gameboy.memory.io.graphics.mode.Vblank
import gameboy.utils.getBit
import gameboy.utils.toHexString
import java.util.*

abstract class Ppu(private val mmu: Mmu) : Memory, Observable() {

    enum class ModeEnum(val mode: Int) {
        HBLANK(0),
        VBLANK(1),
        OAM_SEARCH(2),
        PIXEL_TRANSFER(3),
    }

    // Memory / registers
    abstract val vram: Vram
    val lcdc = Lcdc()
    protected val ly = Register(0xFF44)
    protected val lyc = Register(0xFF45)
    protected val stat = Register(0xFF41)
    protected val scy = Register(0xFF42)
    protected val scx = Register(0xFF43)
    protected val wy = Register(0xFF4A)
    protected val wx = Register(0xFF4B)
    protected val bgp = PaletteDMG()
    protected val obp0 = PaletteDMG()
    protected val obp1 = PaletteDMG()

    val lcd = Lcd()

    // Modes
    private val oamSearch = OamSearch(mmu)
    abstract val pixelTransfer : PixelTransfer
    private val hblank = Hblank()
    private val vblank = Vblank()

    // Internal variables
    private var currentMode : Mode = oamSearch
    private var currentModeEnum = ModeEnum.OAM_SEARCH
    protected var ticksInLine = 0
    protected var currentBank = 0

    override fun reset() {
        currentMode = oamSearch
        currentModeEnum = ModeEnum.OAM_SEARCH
        ticksInLine = 0

        lcdc.value = 0x91
        ly.value = 0x00
        lyc.value = 0x00
        stat.value = 0
        scy.value = 0x00
        scx.value = 0x00
        wy.value = 0x00
        wx.value = 0x00
        bgp.paletteByte = 0xFC
        obp0.paletteByte = 0xFF
        obp1.paletteByte = 0xFF

        vram.reset()
    }

    fun tick(cycles: Int) {
        if (!lcdc.getLcdEnable())
            return

        ++ticksInLine
        currentMode.tick()
        if (currentMode.finished()) {
            when (currentModeEnum) {
                ModeEnum.OAM_SEARCH -> {
                    currentMode = pixelTransfer
                    pixelTransfer.start(oamSearch.sprites)
                    currentModeEnum = ModeEnum.PIXEL_TRANSFER
                }
                ModeEnum.PIXEL_TRANSFER -> {
                    currentMode = hblank
                    hblank.start(ticksInLine)
                    currentModeEnum = ModeEnum.HBLANK
                    requestStatInterrupt(3)
                }
                ModeEnum.HBLANK -> {
                    ticksInLine = 0
                    if (++ly.value == 144) {
                        currentMode = vblank
                        vblank.start()
                        currentModeEnum = ModeEnum.VBLANK
                        mmu.requestInterrupt(Interrupt.VBLANK)
                        requestStatInterrupt(5)
                        requestStatInterrupt(4)
                        lcd.display()
                        lcd.reset()
                    } else {
                        currentMode = oamSearch
                        oamSearch.start()
                        currentModeEnum = ModeEnum.OAM_SEARCH
                        requestStatInterrupt(5)
                    }
                    requestLycEqualsLyInterrupt()
                }
                ModeEnum.VBLANK -> {
                    if (++ly.value == 154) {
                        ticksInLine = 0
                        ly.value = 0
                        currentMode = oamSearch
                        oamSearch.start()
                        currentModeEnum = ModeEnum.OAM_SEARCH
                        requestStatInterrupt(5)
                    } else {
                        vblank.start()
                    }
                    requestLycEqualsLyInterrupt()
                }
            }
        }
    }

    private fun requestStatInterrupt(bit: Int) {
        if (stat.getBit(bit))
            mmu.requestInterrupt(Interrupt.STAT)
    }

    private fun requestLycEqualsLyInterrupt() {
        if (lyc == ly)
            requestStatInterrupt(6)
    }

    fun getMode(): ModeEnum {
        return currentModeEnum
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            lcdc.address -> lcdc.value
            ly.address -> {
                if (lcdc.getLcdEnable()) {
                    ly.value
                } else {
                    0 // If LCD is off this register is fixed at 0
                }
            }
            lyc.address -> lyc.value
            stat.address -> {
                if (lcdc.getLcdEnable()) {
                    stat.value or 0b10000000 or getMode().mode // Bit 7 is always 1
                } else {
                    (stat.value or 0b10000000) and 0b11111000 // Bits 0-2 return 0 when LCD is off
                }
            }
            scy.address -> scy.value
            scx.address -> scx.value
            wy.address -> wy.value
            wx.address -> wx.value
            Mmu.BGP -> this.bgp.paletteByte
            Mmu.OBP0 -> this.obp0.paletteByte
            Mmu.OBP1 -> this.obp1.paletteByte
            in 0x8000 until 0xA000 -> vram.readByte(currentBank, address)
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Lcd")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            lcdc.address -> {
                val lcdBefore = lcdc.getLcdEnable()

                lcdc.value = newVal

                if (lcdBefore && !lcdc.getLcdEnable()) {
                    //ticksInLine = 0
                    //setMode(Mode.HBLANK.mode)
                    //ly.value = 0
                }
            }
            ly.address -> ly.value = newVal
            lyc.address -> lyc.value = newVal
            stat.address -> stat.value = stat.value or (newVal and 0b11111000) // Last three bits are read-only
            scy.address -> scy.value = newVal
            scx.address -> scx.value = newVal
            wx.address -> wx.value = newVal
            wy.address -> wy.value = newVal
            Mmu.BGP -> this.bgp.paletteByte = newVal
            Mmu.OBP0 -> this.obp0.paletteByte = newVal
            Mmu.OBP1 -> this.obp1.paletteByte = newVal
            in 0x8000 until 0xA000 -> vram.writeByte(currentBank, address, newVal)
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Lcd")
        }
    }
}

class PpuCGB(mmu: Mmu) : Ppu(mmu) {

    // Video RAM memory
    override val vram = Vram(2)

    private var bcps = 0
    private var ocps = 0
    private val bgPalettes = Array(8) {PaletteCGB()}
    private val objPalettes = Array(8) {PaletteCGB()}

    private val renderer = PixelRendererCGB(lcd, lcdc, objPalettes, bgPalettes)
    private val fetcher = FetcherCGB(lcdc, wx, wy, scy, scx, ly, mmu.oam, vram)
    override val pixelTransfer = PixelTransfer(renderer, fetcher, lcdc, ly, wy, wx, scx)

    init {
        reset()
    }

    override fun reset() {
        super.reset()

        bcps = 0
        ocps = 0

        // TODO: reset palettes
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.VBK -> currentBank or 0b11111110
            Mmu.BCPS -> bcps or 0b01000000
            Mmu.BCPD -> {
                val index = bcps and 0b00111111
                bgPalettes[index / 8].getByte(index % 8)
            }
            Mmu.OCPS -> ocps or 0b01000000
            Mmu.OCPD -> {
                val index = ocps and 0b00111111
                this.objPalettes[index / 8].getByte(index % 8)
            }
            else -> super.readByte(address)
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.VBK -> currentBank = newVal and 0b00000001
            Mmu.BCPS -> bcps = newVal and 0b10111111
            Mmu.BCPD -> {
                var index = bcps and 0b00111111
                bgPalettes[index / 8].setByte(index % 8, newVal)
                if (bcps.getBit(7)) {
                    index = (index + 1) and 0b111111
                    bcps = (index) or (1 shl 7)
                }
            }
            Mmu.OCPS -> ocps = newVal and 0b10111111
            Mmu.OCPD -> {
                var index = ocps and 0b00111111
                objPalettes[index / 8].setByte(index % 8, newVal)
                if (ocps.getBit(7)) {
                    index = (index + 1) and 0b111111
                    ocps = (index) or (1 shl 7)
                }
            }
            else -> super.writeByte(address, newVal)
        }
    }
}

class PpuDMG(mmu: Mmu) : Ppu(mmu) {
    override val vram = Vram(1)

    private val renderer = PixelRendererDMG(lcd, lcdc, bgp, obp0, obp1)
    private val fetcher = FetcherDMG(lcdc, wx, wy, scy, scx, ly, mmu.oam, vram)
    override val pixelTransfer = PixelTransfer(renderer, fetcher, lcdc, ly, wy, wx, scx)

    init {
        reset()
    }
}