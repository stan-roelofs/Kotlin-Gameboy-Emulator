package gameboy.memory.io.graphics

import gameboy.memory.Mmu

abstract class PixelRenderer (protected val lcd: Lcd, protected val mmu: Mmu) {
    abstract fun renderPixel(bgPixel: Pixel, oamPixel: Pixel?)
}