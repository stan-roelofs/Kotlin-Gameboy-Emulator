package gameboy.cpu

enum class Interrupt {
    VBLANK,
    STAT,
    TIMER,
    SERIAL,
    JOYPAD
}