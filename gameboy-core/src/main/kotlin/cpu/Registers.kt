package cpu

import utils.getBit
import utils.getFirstByte
import utils.getSecondByte
import utils.setBit

/**
 * Created by Stan on 19-Jan-18.
 */

class Registers {
    var A: Int = 0
    set(value) {
        field = value and 0xFF
    }
    var F: Int = 0
        set(value) {
            field = value and 0xF0
        }
    var B: Int = 0
        set(value) {
            field = value and 0xFF
        }
    var C: Int = 0
        set(value) {
            field = value and 0xFF
        }
    var D: Int = 0
        set(value) {
            field = value and 0xFF
        }
    var E: Int = 0
        set(value) {
            field = value and 0xFF
        }
    var H: Int = 0
        set(value) {
            field = value and 0xFF
        }
    var L: Int = 0
        set(value) {
            field = value and 0xFF
        }
    var SP: Int = 0
        set(value) {
            field = value and 0xFFFF
        }
    var PC: Int = 0
        set(value) {
            field = value and 0xFFFF
        }

    val ZFlag: Int = 7
    val NFlag: Int = 6
    val HFlag: Int = 5
    val CFlag: Int = 4

    var IME = false
    var halt = false
    var stop = false
    var haltBug = false

    var clock = 0

    init {
        reset()
    }

    fun reset() {
        A = 0x01
        F = 0xB0
        setBC(0x0013)
        setDE(0x00D8)
        setHL(0x014D)
        SP = 0xFFFE
        PC = 0x100
        IME = true
        halt = false
        stop = false
        haltBug = false
        clock = 0
    }

    fun setZFlag(state: Boolean) {
        F = setBit(F, ZFlag, state)
    }

    fun setNFlag(state: Boolean) {
        F = setBit(F, NFlag, state)
    }

    fun setHFlag(state: Boolean) {
        F = setBit(F, HFlag, state)
    }

    fun setCFlag(state: Boolean) {
        F = setBit(F, CFlag, state)
    }

    fun getZFlag(): Boolean {
        return (F.getBit(ZFlag))
    }

    fun getNFlag(): Boolean {
        return (F.getBit(NFlag))
    }

    fun getHFlag(): Boolean {
        return (F.getBit(HFlag))
    }

    fun getCFlag(): Boolean {
        return (F.getBit(CFlag))
    }

    fun getAF(): Int {
        return (A shl 8) or F
    }

    fun setAF(value: Int) {
        A = value.getSecondByte()
        F = value.getFirstByte()
    }

    fun setBC(value: Int) {
        B = value.getSecondByte()
        C = value.getFirstByte()
    }

    fun setDE(value: Int) {
        D = value.getSecondByte()
        E = value.getFirstByte()
    }

    fun setHL(value: Int) {
        H = value.getSecondByte()
        L = value.getFirstByte()
    }

    fun getBC(): Int {
        return (B shl 8) or C
    }

    fun getDE(): Int {
        return (D shl 8) or E
    }

    fun getHL(): Int {
        return (H shl 8) or L
    }

    fun incPC() {
        PC = (PC + 1)
    }

    fun incSP() {
        SP = (SP + 1)
    }

    fun decSP() {
        SP = (SP - 1)
    }

    override fun toString(): String {
        return "AF=${getAF()} BC=${getBC()} DE=${getDE()} HL=${getHL()} SP=$SP PC=$PC"
    }
}

enum class RegisterID {
    A, F, B, C, D, E, H, L, SP, PC, AF, BC, DE, HL
}