package gameboy.memory.io

import gameboy.memory.Memory
import gameboy.memory.Mmu
import gameboy.memory.io.graphics.Lcd
import gameboy.memory.io.sound.Sound

abstract class IO(mmu: Mmu) : Memory {

    // These are public such that gui can read the LCD data to render,
    // and can send key presses/releases to the Joypad
    abstract val lcd: Lcd
    val joypad = Joypad(mmu)
    val serial = Serial()

    abstract val sound: Sound
    abstract val dma: Dma
    protected abstract val timer: Timer

    protected var IF = 0

    final override fun reset() {
        timer.reset()
        lcd.reset()
        joypad.reset()
        dma.reset()
        serial.reset()
        sound.reset()

        IF = 0x1
    }

    fun tick(cycles: Int) {
        timer.tick(cycles)
        lcd.tick(cycles)
        dma.tick(cycles)
        sound.tick(cycles)
    }
}