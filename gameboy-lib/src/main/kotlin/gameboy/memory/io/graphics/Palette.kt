package gameboy.memory.io.graphics

import java.awt.Color

abstract class Palette {
    /**
     * Given a color index (0-3) for a pixel on the screen,
     * this function returns the color the pixel should get
     * after applying the palette
     *
     * @param index the color index
     * @throws IllegalArgumentException if the index is not in range 0..3
     * @return The color
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

    private val color0 = Color(224, 248, 208)
    private val color1 = Color(136, 192, 112)
    private val color2 = Color(52, 104, 86)
    private val color3 = Color(8, 24, 32)
    private val colors = arrayOf(color0, color1, color2, color3)

    var paletteByte = 0

    override fun applyPalette(color: Int): Color {
        return colors[(paletteByte shr (color * 2)) and 0b11]
    }
}

class PaletteCGB : Palette() {

    private var colors = Array (4) {Color(0,0,0)}

    // Each color is defined by 2 bytes
    // We simply store the 2 bytes in a single int and handle translation
    // In the set/get functions.
    // This makes it easier to extract the values
    private var values = Array(4) {0}

    override fun applyPalette(color: Int): Color {
        return colors[color]
    }

    fun setByte(index: Int, value: Int) {
        if (index !in 0 until 8) {
            throw IllegalArgumentException()
        }

        val colorIndex = index / 2
        if (index % 2 == 0) {
            values[colorIndex] = (values[colorIndex] and 0xFF00) or value
        } else {
            values[colorIndex] = (values[colorIndex] and 0x00FF) or (value shl 8)
        }

        val red = rgb555ToRgb888(value and 0b11111)
        val green = rgb555ToRgb888((value and 0b1111100000) shr 5)
        val blue = rgb555ToRgb888((value and 0b111110000000000) shr 10)
        colors[colorIndex] = Color(red, green, blue)
    }

    fun getByte(index: Int) : Int {
        if (index !in 0 until 8) {
            throw IllegalArgumentException()
        }

        val colorIndex = index / 2
        return if (index % 2 == 0) {
            values[colorIndex] and 0x00FF
        } else {
            (values[colorIndex] and 0xFF00) shr 8
        }
    }

    private fun rgb555ToRgb888(value: Int) : Int {
        return (value shl 3) or (value shr 2)
    }
}