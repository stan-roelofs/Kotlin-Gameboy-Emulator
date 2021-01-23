import gameboy.GameBoy
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

const val MAX_ITERATIONS = 100000000

fun getScreenHash(screen: ByteArray): Int {
    var s = ""
    for (i in screen) {
        s += i.toString()
    }
    return s.hashCode()
}

fun makeScreenshot(outputFile: File, screen: ByteArray) {
    val image = BufferedImage(GameBoy.SCREEN_WIDTH, GameBoy.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB)

    for (y in 0 until GameBoy.SCREEN_HEIGHT) {
        for (x in 0 until GameBoy.SCREEN_WIDTH) {
            val red = screen[y * GameBoy.SCREEN_WIDTH * 3 + x * 3].toInt() and 0xFF
            val green = screen[y * GameBoy.SCREEN_WIDTH * 3 + x * 3 + 1].toInt() and 0xFF
            val blue = screen[y * GameBoy.SCREEN_WIDTH * 3 + x * 3 + 2].toInt() and 0xFF

            val rgb = blue or (green shl 8) or (red shl 16)
            image.setRGB(x, y, rgb)
        }
    }

    ImageIO.write(image, "png", outputFile)
}