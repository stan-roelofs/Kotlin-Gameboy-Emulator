package gameboy.memory.io.graphics

import gameboy.memory.Mmu

class PixelRendererCGB(lcd: Lcd, mmu: Mmu) : PixelRenderer {

    override fun renderPixel(bgPixel: Pixel, oamPixel: Pixel?) {
        /*
        if (!mmu.readByte(Mmu.LCDC).getBit(0)) {// Lcd disabled
            bgPixel.color = 0
        }

        // BG pixel is used when
        // - There is no oam pixel, or
        // - The oam pixel is transparent (color 0)
        // - The OBJ-to-BG priority (0 = OBJ above BG, 1 = OBJ behind BG color 1-3) is set (oamPixel.priority) and the BG pixel is not transparent
        val color = if (oamPixel == null || oamPixel.color == 0 || (oamPixel.priority && bgPixel.color != 0)) {
            val palette = mmu.readByte(Mmu.BGP)
            (palette shr (bgPixel.color * 2)) and 0b11
        } else {
            val palette = if ((oamPixel as PixelDMG).palette1) mmu.readByte(Mmu.OBP1) else mmu.readByte(Mmu.OBP0)
            (palette shr (oamPixel.color * 2)) and 0b11
        }

        lcd.pushPixel(colors[color].red, colors[color].green, colors[color].blue)*/
    }
}