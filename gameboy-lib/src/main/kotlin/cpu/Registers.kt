package cpu

import utils.getBit
import utils.getFirstByte
import utils.getSecondByte
import utils.setBit

/**
 * Represents the registers of the gameboy CPU
 *
 * 8-bit registers: A, F, B, C, D, E, H, L
 * 16-bit registers: SP, PC
 *
 * On construction [reset] is called to set the registers to the initial values.
 */
class Registers {

    /** Register A (8-bit) */
    var A: Int = 0
        internal set(value) {
        field = value and 0xFF
    }

    /** Register F (8-bit) */
    var F: Int = 0
        internal set(value) {
            field = value and 0xF0
        }

    /** Register B (8-bit) */
    var B: Int = 0
        internal set(value) {
            field = value and 0xFF
        }

    /** Register C (8-bit) */
    var C: Int = 0
        internal set(value) {
            field = value and 0xFF
        }

    /** Register D (8-bit) */
    var D: Int = 0
        internal set(value) {
            field = value and 0xFF
        }

    /** Register E (8-bit) */
    var E: Int = 0
        internal set(value) {
            field = value and 0xFF
        }

    /** Register H (8-bit) */
    var H: Int = 0
        internal set(value) {
            field = value and 0xFF
        }

    /** Register L (8-bit) */
    var L: Int = 0
        internal set(value) {
            field = value and 0xFF
        }

    /** Stack pointer (16-bit) */
    var SP: Int = 0
        internal set(value) {
            field = value and 0xFFFF
        }

    /** Program counter (16-bit) */
    var PC: Int = 0
        internal set(value) {
            field = value and 0xFFFF
        }

    internal val ZFlag: Int = 7
    private val NFlag: Int = 6
    private val HFlag: Int = 5
    internal val CFlag: Int = 4

    /** IME (Interrupt Master Enable) flag.
     * Disables / enables all interrupts
     */
    var IME = false
        internal set

    /** Halt flag, enabled when the cpu is in the halt state */
    var halt = false
        internal set

    /** Stop flag, enabled when the cpu is in the STOP state */
    var stop = false
        internal set

    var haltBug = false
        internal set

    var eiExecuted = false
        internal set

    init {
        reset()
    }

    /**
     * Sets the registers to the values they should be after running the boot rom
     */
    fun reset() {
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

    internal fun setZFlag(state: Boolean) {
        F = setBit(F, ZFlag, state)
    }

    internal fun setNFlag(state: Boolean) {
        F = setBit(F, NFlag, state)
    }

    internal fun setHFlag(state: Boolean) {
        F = setBit(F, HFlag, state)
    }

    internal fun setCFlag(state: Boolean) {
        F = setBit(F, CFlag, state)
    }

    /** Returns the state of the Z flag the flags registers */
    fun getZFlag(): Boolean {
        return (F.getBit(ZFlag))
    }

    /** Returns the state of the N flag the flags registers */
    fun getNFlag(): Boolean {
        return (F.getBit(NFlag))
    }

    /** Returns the state of the H flag the flags registers */
    fun getHFlag(): Boolean {
        return (F.getBit(HFlag))
    }

    /** Returns the state of the C flag the flags registers */
    fun getCFlag(): Boolean {
        return (F.getBit(CFlag))
    }

    /** Returns the combined register AF (16 bit) */
    fun getAF(): Int {
        return (A shl 8) or F
    }

    internal fun setAF(value: Int) {
        A = value.getSecondByte()
        F = value.getFirstByte()
    }

    internal fun setBC(value: Int) {
        B = value.getSecondByte()
        C = value.getFirstByte()
    }

    internal fun setDE(value: Int) {
        D = value.getSecondByte()
        E = value.getFirstByte()
    }

    internal fun setHL(value: Int) {
        H = value.getSecondByte()
        L = value.getFirstByte()
    }

    /** Returns the combined register BC (16 bit) */
    fun getBC(): Int {
        return (B shl 8) or C
    }

    /** Returns the combined register DE (16 bit) */
    fun getDE(): Int {
        return (D shl 8) or E
    }

    /** Returns the combined register HL (16 bit) */
    fun getHL(): Int {
        return (H shl 8) or L
    }

    internal fun incPC() {
        PC = (PC + 1)
    }

    internal fun incSP() {
        SP = (SP + 1)
    }

    internal fun decSP() {
        SP = (SP - 1)
    }

    override fun toString(): String {
        return "AF=${getAF()} BC=${getBC()} DE=${getDE()} HL=${getHL()} SP=$SP PC=$PC"
    }
}

enum class RegisterID {
    A, F, B, C, D, E, H, L, SP, PC, AF, BC, DE, HL
}