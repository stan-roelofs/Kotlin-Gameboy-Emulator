package nl.stanroelofs.gameboy.cpu

import nl.stanroelofs.gameboy.utils.firstByte
import nl.stanroelofs.gameboy.utils.getBit
import nl.stanroelofs.gameboy.utils.secondByte
import nl.stanroelofs.gameboy.utils.setBit

/**
 * Represents the registers of the gameboy CPU
 *
 * 8-bit registers: A, F, B, C, D, E, H, L
 * 16-bit registers: SP, PC
 *
 * On construction [reset] is called to set the registers to the initial values.
 */
abstract class Registers {

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

    /** The combined register AF (16 bit) */
    var AF: Int
        get() {
            return (A shl 8) or F
        }
        internal set(value) {
            A = value.secondByte
            F = value.firstByte
        }

    /** The combined register BC (16 bit) */
    var BC: Int
        get() {
            return (B shl 8) or C
        }
        internal set(value) {
            B = value.secondByte
            C = value.firstByte
        }

    /** The combined register DE (16 bit) */
    var DE: Int
        get() {
            return (D shl 8) or E
        }
        internal set(value) {
            D = value.secondByte
            E = value.firstByte
        }

    /** The combined register HL (16 bit) */
    var HL: Int
        get() {
            return (H shl 8) or L
        }
        internal set(value) {
            H = value.secondByte
            L = value.firstByte
        }

    internal val ZFlag: Int = 7
    internal val NFlag: Int = 6
    internal val HFlag: Int = 5
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

    internal var haltBug = false
    internal var eiExecuted = false

    /**
     * Sets the registers to the values they should be after running the boot rom
     */
    abstract fun reset()

    internal fun setZFlag(state: Boolean) {
        F = F.setBit(ZFlag, state)
    }

    internal fun setNFlag(state: Boolean) {
        F = F.setBit(NFlag, state)
    }

    internal fun setHFlag(state: Boolean) {
        F = F.setBit(HFlag, state)
    }

    internal fun setCFlag(state: Boolean) {
        F = F.setBit(CFlag, state)
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
        return "AF=$AF BC=$BC DE=$DE HL=$HL SP=$SP PC=$PC"
    }
}