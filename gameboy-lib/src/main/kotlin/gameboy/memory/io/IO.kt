package gameboy.memory.io

import gameboy.memory.Memory
import gameboy.memory.Mmu
import gameboy.memory.io.graphics.Ppu
import gameboy.memory.io.sound.Sound

abstract class IO(mmu: Mmu) : Memory {

    // These are public such that gui can read the LCD data to render,
    // and can send key presses/releases to the Joypad
    abstract val ppu: Ppu
    val joypad = Joypad(mmu)
    val serial = Serial()

    abstract val sound: Sound
    abstract val dma: Dma
    protected abstract val timer: Timer

    protected var IF = 0

    final override fun reset() {
        timer.reset()
        ppu.reset()
        joypad.reset()
        dma.reset()
        serial.reset()
        sound.reset()

        IF = 0x1
    }

    open fun tick(cycles: Int) {
        timer.tick(cycles)
        ppu.tick(cycles)
        dma.tick(cycles)
        sound.tick(cycles)
    }
}