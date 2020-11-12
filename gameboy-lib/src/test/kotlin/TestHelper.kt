import gameboy.GameBoy
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

val MAX_ITERATIONS = 100000000

fun getScreenHash(screen: IntArray): Int {
    var s = ""
    for (i in screen) {
        s += i.toString()
    }
    return s.hashCode()
}

fun makeScreenshot(outputFile: File, screen: IntArray) {
    val image = BufferedImage(GameBoy.SCREEN_WIDTH, GameBoy.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB)

    for (y in 0 until GameBoy.SCREEN_HEIGHT) {
        for (x in 0 until GameBoy.SCREEN_WIDTH) {
            val red = screen[y * GameBoy.SCREEN_WIDTH * 3 + x * 3] * 255 / 31
            val green = screen[y * GameBoy.SCREEN_WIDTH * 3 + x * 3 + 1] * 255 / 31
            val blue = screen[y * GameBoy.SCREEN_WIDTH * 3 + x * 3 + 2] * 255 / 31

            val rgb = blue or (green shl 8) or (red shl 16)
            image.setRGB(x, y, rgb)
        }
    }

    ImageIO.write(image, "png", outputFile)
}