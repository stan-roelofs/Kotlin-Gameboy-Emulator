package gameboy.memory.io.graphics.mode

import gameboy.memory.Mmu
import gameboy.memory.io.graphics.*


class PixelTransfer(private val lcdc: Lcdc, private val renderer: PixelRenderer, cgb: Boolean, private val mmu: Mmu) : Mode {

    private var sprites = Array<SpritePosition?>(10) { null }
    private var bgFifo = Fifo<Pixel>(16)
    private var oamFifo = Fifo<Pixel>(16)
    private var fetcher = if (cgb) FetcherCGB(bgFifo, oamFifo, mmu) else FetcherDMG(bgFifo, oamFifo, mmu) // TODO: dependency injection

    private var x = 0

    // The number of pixels we dropped
    private var droppedPixels = 0
    // Did we start rendering the window?
    private var window = false

    fun start(sprites: Array<SpritePosition?>) {
        this.sprites = sprites

        droppedPixels = 0
        window = false

        bgFifo.clear()
        oamFifo.clear()
        fetcher.reset()

        x = 0

        if (lcdc.getBGWindowDisplay()) {
            fetcher.startFetchingBackground()
        }
    }

    override fun tick() {
        fetcher.tick()

        if (lcdc.getBGWindowDisplay()) { // Bg/window enabled
            if (!bgFifo.empty && droppedPixels < mmu.readByte(Mmu.SCX) % 8) { // When SCX is not divisible by 8, we should drop the first few pixels
                bgFifo.pop()
                droppedPixels++
                return
            }

            // If we have not processed the window, and window is enabled, and LY == WY, and current x == WX
            if (!window && lcdc.getWindowEnable() && mmu.readByte(Mmu.LY) >= mmu.readByte(Mmu.WY) && x == mmu.readByte(Mmu.WX) - 7) {
                window = true
                fetcher.startFetchingWindow()
                return
            }
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

        if (x < 160 && !bgFifo.empty) {
            ++x
            renderer.renderPixel(bgFifo.pop(), if (oamFifo.empty) null else oamFifo.pop())
        }
    }

    override fun finished(): Boolean {
        return x == 160
    }
}