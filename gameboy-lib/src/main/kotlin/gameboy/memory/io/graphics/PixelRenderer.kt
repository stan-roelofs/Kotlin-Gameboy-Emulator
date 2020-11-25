package gameboy.memory.io.graphics

interface PixelRenderer {
    fun renderPixel(bgPixel: Pixel, oamPixel: Pixel?)
}