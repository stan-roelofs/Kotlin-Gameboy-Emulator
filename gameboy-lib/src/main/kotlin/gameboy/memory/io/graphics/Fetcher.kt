package gameboy.memory.io.graphics

import gameboy.memory.Mmu

open class Fetcher(protected val bgFifo: Fifo<Pixel>, protected val oamFifo : Fifo<Pixel>, protected val mmu: Mmu)