package gameboy.memory.io.graphics

import gameboy.memory.Mmu

abstract class Fetcher(protected val bgFifo: Fifo<Pixel>, protected val oamFifo : Fifo<Pixel>, protected val mmu: Mmu) {
    abstract fun reset()
    abstract fun startFetchingBackground()
    abstract fun startFetchingWindow()
    abstract fun startFetchingSprite(sprite: SpritePosition)
    abstract fun fetchingSprite(): Boolean
    abstract fun tick()
}