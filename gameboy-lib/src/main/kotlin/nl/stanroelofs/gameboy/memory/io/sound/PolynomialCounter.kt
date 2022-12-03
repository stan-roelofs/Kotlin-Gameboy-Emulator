package nl.stanroelofs.gameboy.memory.io.sound

import nl.stanroelofs.gameboy.utils.getBit
import nl.stanroelofs.gameboy.utils.setBit

class PolynomialCounter {

    private var clockShift = 0
    private var divisorCode = 0
    private var shiftedDivisor = 0
    private var counter = 0
    var width7 = false

    init {
        reset()
    }

    fun reset() {
        shiftedDivisor = 0
        counter = 0
        clockShift = 0
        divisorCode = 0
        width7 = false
    }

    fun getNr43(): Int {
        var result = clockShift shl 4
        result = result.setBit(3, width7)
        result = result or divisorCode
        return result
    }

    fun setNr43(value: Int) {
        width7 = value.getBit(3)
        clockShift = value shr 4

        divisorCode = value and 0b111
        val divisor = if (divisorCode == 0) {
            8
        } else {
            divisorCode shl 4
        }

        shiftedDivisor = divisor shl clockShift
    }

    fun tick(): Boolean {
        counter--
        return if (counter == 0) {
            counter = shiftedDivisor * 4
            true
        } else {
            false
        }
    }

    fun trigger() {
        counter = shiftedDivisor * 4
    }
}