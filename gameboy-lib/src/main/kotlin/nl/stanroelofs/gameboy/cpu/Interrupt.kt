package nl.stanroelofs.gameboy.cpu

enum class Interrupt {
    VBLANK,
    STAT,
    TIMER,
    SERIAL,
    JOYPAD
}