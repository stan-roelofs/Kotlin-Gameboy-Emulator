package gameboy.memory.io.graphics.mode

import gameboy.memory.Register
import gameboy.memory.io.graphics.*


class PixelTransfer(private val renderer: PixelRenderer, private val fetcher: Fetcher, private val lcdc: Lcdc, private val ly: Register,
                    private val wy: Register, private val wx: Register, private val scx: Register) : Mode {

    private var sprites = Array<SpritePosition?>(10) { null }
    private var x = 0

    // The number of pixels we dropped
    private var droppedPixels = 0
    // Did we start rendering the window?
    private var window = false

    fun start(sprites: Array<SpritePosition?>) {
        this.sprites = sprites

        droppedPixels = 0
        window = false

        fetcher.reset()

        x = 0

        if (lcdc.getBGWindowDisplay()) {
            fetcher.startFetchingBackground()
        }
    }

    override fun tick() {
        fetcher.tick()

        if (!fetcher.bgFifo.empty && droppedPixels < scx.value % 8) { // When SCX is not divisible by 8, we should drop the first few pixels
            fetcher.bgFifo.pop()
            droppedPixels++
            return
        }

        // If we have not processed the window, and window is enabled, and LY == WY, and current x == WX
        if (!window && lcdc.getWindowEnable() && ly.value >= wy.value && x == wx.value - 7) {
            window = true
            fetcher.startFetchingWindow()
            return
        }

        if (lcdc.getObjectEnable()) { // OBJ display enabled
            if (fetcher.fetchingSprite())
                return

            for (i in sprites.indices) {
                if (sprites[i]?.x == x + 8) {
                    fetcher.startFetchingSprite(sprites[i]!!)
                    sprites[i] = null
                    return
                }
            }
        }

        if (x < 160 && !fetcher.bgFifo.empty) {
            ++x
            renderer.renderPixel(fetcher.bgFifo.pop(), if (fetcher.oamFifo.empty) null else fetcher.oamFifo.pop())
        }
    }

    override fun finished(): Boolean {
        return x == 160
    }
}