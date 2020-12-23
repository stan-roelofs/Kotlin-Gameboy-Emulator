package gameboy.memory.io.graphics

import gameboy.cpu.Interrupt
import gameboy.memory.Memory
import gameboy.memory.Mmu
import gameboy.memory.Register
import gameboy.memory.io.graphics.mode.Hblank
import gameboy.memory.io.graphics.mode.OamSearch
import gameboy.memory.io.graphics.mode.PixelTransfer
import gameboy.memory.io.graphics.mode.Vblank
import java.util.*

abstract class Ppu(private val mmu: Mmu) : Memory, Observable() {

    enum class ModeEnum(val mode: Int) {
        HBLANK(0),
        VBLANK(1),
        OAM_SEARCH(2),
        PIXEL_TRANSFER(3),
    }

    abstract val vram: Vram
    protected var lcdc = Lcdc()
    protected var ly = Register(0xFF44)
    protected var lyc = Register(0xFF45)
    protected var stat = Register(0xFF41)
    protected var scy = Register(0xFF42)
    protected var scx = Register(0xFF43)
    protected var wy = Register(0xFF4A)
    protected var wx = Register(0xFF4B)
    protected val bgp = PaletteDMG()
    protected val obp0 = PaletteDMG()
    protected val obp1 = PaletteDMG()

    protected var ticksInLine = 0

    private val oamSearch = OamSearch(mmu)
    val lcd = Lcd()
    abstract val pixelTransfer : PixelTransfer
    private val hblank = Hblank()
    private val vblank = Vblank()

    private var currentMode : Mode = oamSearch
    private var currentModeEnum = ModeEnum.OAM_SEARCH

    override fun reset() {
        currentMode = oamSearch
        currentModeEnum = ModeEnum.OAM_SEARCH
        ticksInLine = 0
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
}