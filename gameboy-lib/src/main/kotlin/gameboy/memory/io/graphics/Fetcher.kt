package gameboy.memory.io.graphics

import gameboy.memory.Oam
import gameboy.memory.Register

abstract class Fetcher(protected val lcdc: Lcdc, protected val wx: Register, protected val wy: Register, protected val scy: Register,
                       protected val scx: Register, protected val ly: Register, protected val oam: Oam, protected val vram: Vram) {

    val bgFifo = Fifo<Pixel>(16)
    val oamFifo = Fifo<Pixel>(16)

    abstract fun reset()
    abstract fun startFetchingBackground()
    abstract fun startFetchingWindow()
    abstract fun startFetchingSprite(sprite: SpritePosition)
    abstract fun fetchingSprite(): Boolean
    abstract fun tick()
}