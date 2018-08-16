package memory

import utils.getBit

class Timer : Memory {
    private val divCycles = 256
    private var divCounter = 0
    private var DIV = 0

    private var TIMA = 0
    private var TMA = 0
    private var TAC = 0

    private val clock0Cycles = 1024
    private val clock1Cycles = 16
    private val clock2Cycles = 64
    private val clock3Cycles = 256

    private var timerCycles = clock0Cycles
    private var timerCounter = 0

    override fun reset() {
        divCounter = 0
        timerCounter = 0
        timerCycles = clock0Cycles
        DIV = 0
        TIMA = 0
        TMA = 0
        TAC = 0
    }

    fun tick(cycles: Int) {
        // update DIV
        divCounter += cycles
        while (divCounter >= divCycles) {
            divCounter -= divCycles
            DIV++
            if (DIV > 0xFF) {
                DIV = 0
            }
        }

        // update timer
        // If timer enabled
        if (TAC.getBit(2)) {
            timerCounter += cycles

            while (timerCounter >= timerCycles) {
                timerCounter -= timerCycles
                TIMA++

                if (TIMA > 0xFF) {
                    TIMA = TMA
                    requestInterrupt(2)
                }
            }
        }
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.DIV -> this.DIV
            Mmu.TIMA -> this.TIMA
            Mmu.TMA -> this.TMA
            Mmu.TAC -> this.TAC or 0b11111000
            else -> throw IllegalArgumentException("Address $address does not belong to Timer")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF

        when(address) {
            Mmu.DIV -> this.DIV = 0
            Mmu.TIMA -> this.TIMA = newVal
            Mmu.TMA -> this.TMA = newVal
            Mmu.TAC -> {
                this.TAC = newVal

                val freq = newVal and 0b11
                when (freq) {
                    0b00 -> timerCycles = clock0Cycles
                    0b01 -> timerCycles = clock1Cycles
                    0b10 -> timerCycles = clock2Cycles
                    0b11 -> timerCycles = clock3Cycles
                }
            }
            else -> throw IllegalArgumentException("Address $address does not belong to Timer")
        }
    }
}