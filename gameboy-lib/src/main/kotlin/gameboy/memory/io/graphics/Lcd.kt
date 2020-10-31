package gameboy.memory.io.graphics

import gameboy.memory.Memory
import gameboy.memory.Mmu
import gameboy.utils.getBit
import gameboy.utils.setBit
import java.util.*

abstract class Lcd(private val mmu: Mmu) : Memory, Observable() {

    // Internal screen buffer
    abstract val screenBuffer: Array<IntArray>
    val backgroundBuffer = Array(144) {IntArray(160)}
    val windowBuffer = Array(144) {IntArray(160)}
    val spritesBuffer = Array(144) {IntArray(160)}

    var lastFrame = Array(144) {IntArray(160)}

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

    protected var cycleCounter = 0
    protected var isAnyStat = false

    fun tick(cyclesElapsed: Int) {
       // if (!lcdEnabled()) {
       //     return
       // }

        cycleCounter += cyclesElapsed

        val mode = getMode()
        when (mode) {
            Mode.OAM_SEARCH.mode -> {
                if (cycleCounter >= Mode.OAM_SEARCH.cycles) {
                    cycleCounter = 0
                    setMode(Mode.LCD_TRANSFER)
                }
            }
            Mode.LCD_TRANSFER.mode -> {
                if (cycleCounter >= Mode.LCD_TRANSFER.cycles) {
                    cycleCounter = 0
                    setMode(Mode.HBLANK)

                    renderScanline()
                }
            }
            Mode.HBLANK.mode -> {
                if (cycleCounter >= Mode.HBLANK.cycles) {
                    cycleCounter = 0
                    LY++

                    if (LY == 144) {
                        setMode(Mode.VBLANK)

                        mmu.requestInterrupt(0)

                        setChanged()
                        lastFrame = Array(144) {
                            screenBuffer[it].clone()
                        }
                        notifyObservers(lastFrame)

                    } else {
                        setMode(Mode.OAM_SEARCH)
                    }
                }
            }
            Mode.VBLANK.mode -> {
                if (cycleCounter >= Mode.VBLANK.cycles) {
                    cycleCounter = 0
                    LY++

                    if (LY > 153) {
                        LY = 0
                        setMode(Mode.OAM_SEARCH)
                    }
                }
            }

        }

        STAT = setBit(STAT, 2, LY == LYC)

        checkStatInterrupts()
    }

    private fun checkStatInterrupts() {
        val mode = getMode()

        if (    (mode == Mode.HBLANK.mode && STAT.getBit(3)) ||
                (mode == Mode.OAM_SEARCH.mode && STAT.getBit(5)) ||
                (mode == Mode.VBLANK.mode && (STAT.getBit(4) || STAT.getBit(5))) ||
                (STAT.getBit(6) && STAT.getBit(2))
        ) {
            if (!isAnyStat) {
                isAnyStat = true
                mmu.requestInterrupt(1)
            }
        } else {
            isAnyStat = false
        }
    }

    protected fun setMode(mode: Mode) {
        val nr = mode.mode
        STAT = setBit(STAT, 0, nr.getBit(0))
        STAT = setBit(STAT, 1, nr.getBit(1))
    }

    private fun getMode(): Int {
        return STAT and 0b11
    }

    private fun renderScanline() {
        val row = IntArray(160)
        renderBackground(row)
        renderWindow(row)
        renderSprites(row)
    }

    protected abstract fun renderWindow(row: IntArray)

    protected abstract fun renderBackground(row: IntArray)

    protected abstract fun renderSprites(row: IntArray)

    internal abstract fun updateSprite(address: Int, value: Int)

    internal abstract fun updateTile(address: Int)

    private fun lcdEnabled(): Boolean {
        return LCDC.getBit(7)
    }
}