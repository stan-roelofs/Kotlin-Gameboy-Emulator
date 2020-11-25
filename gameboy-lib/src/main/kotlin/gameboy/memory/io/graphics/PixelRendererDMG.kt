package gameboy.memory.io.graphics

class PixelRendererDMG(private val lcd: Lcd, private val lcdc : Lcdc, private val bgp : PaletteDMG, private val obp0 : PaletteDMG,
                        private val obp1 : PaletteDMG) : PixelRenderer {

    override fun renderPixel(bgPixel: Pixel, oamPixel: Pixel?) {
        if (!lcdc.getBGWindowDisplay()) {
            bgPixel.color = 0
        }

        // BG pixel is used when
        // - There is no oam pixel, or
        // - The oam pixel is transparent (color 0)
        // - The OBJ-to-BG priority (0 = OBJ above BG, 1 = OBJ behind BG color 1-3) is set (oamPixel.priority) and the BG pixel is not transparent
        val color = if (oamPixel == null || oamPixel.color == 0 || (oamPixel.priority && bgPixel.color != 0)) {
            bgp.getColor(bgPixel.color)
        } else if ((oamPixel as PixelDMG).palette1) {
            obp1.getColor(oamPixel.color)
        } else {
            obp0.getColor(oamPixel.color)
        }

        lcd.pushPixel(color.red, color.green, color.blue)
    }
}