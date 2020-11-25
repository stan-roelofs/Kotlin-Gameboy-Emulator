package gameboy.memory.io.graphics

import java.awt.Color

abstract class Palette {
    /**
     * Given a color index (0-3) for a pixel on the screen,
     * this function returns the color the pixel should get
     * after applying the palette
     */
    fun getColor(index: Int): Color {
        if (index !in 0..3) {
            throw IllegalArgumentException("Color index should be in range 0..3")
        }

        return applyPalette(index)
    }

    abstract fun applyPalette(color: Int): Color
}

class PaletteDMG : Palette() {

    private val color0 = Color(28, 31, 26)
    private val color1 = Color(17, 24, 14)
    private val color2 = Color(6, 13, 10)
    private val color3 = Color(1, 3, 4)
    private val colors = arrayOf(color0, color1, color2, color3)

    var paletteByte = 0

    override fun applyPalette(color: Int): Color {
        return colors[(paletteByte shr (color * 2)) and 0b11]
    }
}