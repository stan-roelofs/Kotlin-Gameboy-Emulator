import gameboy.GameBoy
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

val MAX_ITERATIONS = 100000000

fun getScreenHash(screen: Array<IntArray>): Int {
    var s = ""
    for (i in screen) {
        for (j in i) {
            s += j.toString()
        }
    }
    return s.hashCode()
}

fun makeScreenshot(outputFile: File, screen: Array<IntArray>) {
    val image = BufferedImage(GameBoy.SCREEN_WIDTH, GameBoy.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB)

    for (i in 0 until GameBoy.SCREEN_HEIGHT) {
        for (j in 0 until GameBoy.SCREEN_WIDTH) {
            val red = screen[i][j * 3] * 255 / 31
            val green = screen[i][j * 3 + 1] * 255 / 31
            val blue = screen[i][j * 3 + 2] * 255 / 31

            val rgb = blue or (green shl 8) or (red shl 16)
            image.setRGB(j, i, rgb)
        }
    }

    ImageIO.write(image, "png", outputFile)
}