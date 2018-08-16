package gui

import GameBoy
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import tornadofx.View
import tornadofx.imageview
import tornadofx.vbox
import utils.getBit

class VRamView(private val gb: GameBoy): View() {

    val color0 = Color(224f / 255.0, 248f / 255.0, 208f / 255.0, 1.0)
    val color1 = Color(136f / 255.0, 192f / 255.0, 112f / 255.0, 1.0)
    val color2 = Color(52f / 255.0, 104f / 255.0, 86f / 255.0, 1.0)
    val color3 = Color(8f / 255.0, 24f / 255.0, 32f / 255.0, 1.0)
    val colors = arrayOf(color0, color1, color2, color3)

    private var vram = WritableImage(256, 392)

    private var counter = 0
    private val framesWait = 60

    override val root = vbox {
        imageview(vram)
    }

    fun update() {
        // Only update every framesWait frames
        if (counter >= 1) {
            if (counter >= framesWait) {
                counter = 0
                return
            }
            counter++
            return
        }

        // Get pixelWriter
        val pixelWriter = vram.pixelWriter

        // Get mmu instance to read memory
        val mmu = gb.mmu

        val screen = Array(128) {IntArray(192)}

        for (tiley in 0 until 24) {
            for (tilex in 0 until 16) {
                val addressStart = 0x8000 + tilex * 16 + tiley * 256

                for (y in 0 until 8) {
                    val byte1 = mmu.readByte(addressStart + y * 2)
                    val byte2 = mmu.readByte(addressStart + y * 2 + 1)

                    for (x in 0 until 8) {
                        val LSB = if (byte1.getBit(x)) 1 else 0
                        val MSB = if (byte2.getBit(x)) 2 else 0
                        val color = LSB + MSB

                        // Render at twice the original resolution
                        screen[tilex * 8 + (7 - x)][tiley * 8 + y] = color
                    }
                }
            }
        }

        for (x in 0 until 128) {
            for (y in 0 until 192) {
                pixelWriter.setColor(2 * x, 2 * y, colors[screen[x][y]])
                pixelWriter.setColor(2 * x + 1, 2 * y, colors[screen[x][y]])
                pixelWriter.setColor(2 * x, 2 * y + 1, colors[screen[x][y]])
                pixelWriter.setColor(2 * x + 1, 2 * y + 1, colors[screen[x][y]])
            }
        }

        counter++
    }
}