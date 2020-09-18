package gameboy.cpu

class RegistersDMG : Registers() {

    init {
        reset()
    }

    override fun reset() {
        A = 0x01
        F = 0xB0
        setBC(0x0013)
        setDE(0x00D8)
        setHL(0x014D)
        SP = 0xFFFE
        PC = 0x100
        stop = false
        halt = false
        IME = false
    }
}