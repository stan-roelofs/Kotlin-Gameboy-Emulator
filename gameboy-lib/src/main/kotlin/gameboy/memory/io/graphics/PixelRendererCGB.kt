package gameboy.memory.io.graphics

class PixelRendererCGB(private val lcd: Lcd, private val lcdc: Lcdc, private val objPalettes: Array<PaletteCGB>,
                        private val bgPalettes: Array<PaletteCGB>) : PixelRenderer {

    init {
        if (objPalettes.size != 8 || bgPalettes.size != 8) {
            throw IllegalArgumentException("Invalid palette array size")
        }
    }

    override fun renderPixel(bgPixel: Pixel, oamPixel: Pixel?) {
        if (!lcdc.getBGWindowDisplay()) {
            bgPixel.color = 0
        }

        // BG pixel is used when
        // - There is no oam pixel, or
        // - The oam pixel is transparent (color 0)
        // - The OBJ-to-BG priority (0 = OBJ above BG, 1 = OBJ behind BG color 1-3) is set (oamPixel.priority) and the BG pixel is not transparent
        val masterPriority = lcdc.getBGWindowDisplay()
        val color = if (oamPixel == null || oamPixel.color == 0 || (!masterPriority && oamPixel.priority && bgPixel.color != 0)) {
            bgPalettes[(bgPixel as PixelCGB).palette].getColor(bgPixel.color)
        } else {
            objPalettes[(oamPixel as PixelCGB).palette].getColor(oamPixel.color)
        }

        lcd.pushPixel(color.red, color.green, color.blue)
    }
}