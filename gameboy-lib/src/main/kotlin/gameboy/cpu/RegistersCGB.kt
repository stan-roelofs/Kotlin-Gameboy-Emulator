package gameboy.cpu

class RegistersCGB : Registers() {

    init {
        reset()
    }

    override fun reset() {
        A = 0x011
        F = 0x80
        setBC(0x0000)
        setDE(0xFF56)
        setHL(0x000D)
        SP = 0xFFFE
        PC = 0x100
        stop = false
        halt = false
        IME = false
    }
}