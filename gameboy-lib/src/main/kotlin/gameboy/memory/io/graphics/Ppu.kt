package gameboy.memory.io.graphics

import gameboy.cpu.Interrupt
import gameboy.memory.Memory
import gameboy.memory.Mmu
import gameboy.memory.io.graphics.mode.Hblank
import gameboy.memory.io.graphics.mode.OamSearch
import gameboy.memory.io.graphics.mode.PixelTransfer
import gameboy.memory.io.graphics.mode.Vblank
import gameboy.utils.getBit
import java.util.*

abstract class Ppu(private val mmu: Mmu) : Memory, Observable() {

    enum class ModeEnum(val mode: Int) {
        HBLANK(0),
        VBLANK(1),
        OAM_SEARCH(2),
        PIXEL_TRANSFER(3),
    }

    protected var LCDC = 0
    protected var LY = 0
    protected var LYC = 0
    protected var STAT = 0
    protected var SCY = 0
    protected var SCX = 0
    protected var WY = 0
    protected var WX = 0
    protected var BGP = 0
    protected var OBP0 = 0
    protected var OBP1 = 0

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
        if (!lcdEnabled())
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
                    if (++LY == 144) {
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
                    if (++LY == 154) {
                        ticksInLine = 0
                        LY = 0
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
        if (STAT.getBit(bit))
            mmu.requestInterrupt(Interrupt.STAT)
    }

    private fun requestLycEqualsLyInterrupt() {
        if (LYC == LY)
            requestStatInterrupt(6)
    }

    fun getMode(): ModeEnum {
        return currentModeEnum
    }

    fun lcdEnabled(): Boolean {
        return LCDC.getBit(7)
    }
}