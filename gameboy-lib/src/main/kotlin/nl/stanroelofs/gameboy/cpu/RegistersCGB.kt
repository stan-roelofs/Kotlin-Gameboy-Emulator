package nl.stanroelofs.gameboy.cpu

class RegistersCGB : Registers() {

    init {
        reset()
    }

    override fun reset() {
        A = 0x011
        F = 0x80
        BC = 0x0000
        DE = 0xFF56
        HL = 0x000D
        SP = 0xFFFE
        PC = 0x100
        stop = false
        halt = false
        IME = false
    }
}