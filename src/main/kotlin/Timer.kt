class Timer(private val mmu: Mmu) {

    private val DIV = 0xFF04
    private val TIMA = 0xFF05
    private val TMA = 0xFF06
    private val TAC = 0xFF07

    private val divCycles = 256
    private var divCounter = 0

    private val clock0Cycles = 1024
    private val clock1Cycles = 16
    private val clock2Cycles = 64
    private val clock3Cycles = 256

    private var timerCycles = clock0Cycles
    private var timerCounter = 0

    fun tick(cycles: Int) {
        // update DIV
        divCounter += cycles
        var div = mmu.readByte(DIV)

        while (divCounter >= divCycles) {
            divCounter -= divCycles
            div++
            if (div > 0xFF) {
                div = 0
            }
        }
        // TODO
        mmu.io[DIV - 0xFF00] = div

        // update timer
        // If timer enabled
        if (mmu.readByte(TAC).getBit(2)) {

            val freq = mmu.readByte(TAC) and 0b11
            when (freq) {
                0b00 -> timerCycles = clock0Cycles
                0b01 -> timerCycles = clock1Cycles
                0b10 -> timerCycles = clock2Cycles
                0b11 -> timerCycles = clock3Cycles
            }

            timerCounter += cycles
            var tima = mmu.readByte(TIMA)
            while (timerCounter >= timerCycles) {
                timerCounter -= timerCycles
                tima++

                if (tima > 0xFF) {
                    tima = mmu.readByte(TMA)
                    var IF = mmu.readByte(0xFF0F)
                    IF = setBit(IF, 2)
                    mmu.writeByte(0xFF0F, IF)
                }
            }

            mmu.writeByte(TIMA, tima)
        }
    }
}