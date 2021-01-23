package gameboy.memory

import gameboy.utils.getBit

open class Register(val address: Int, initialValue: Int = 0) {
    var value = initialValue
        set(value) {
            field = value and 0xFF
        }

    operator fun inc(): Register {
        value++
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other is Register) {
            return value == other.value
        }

        if (other is Int) {
            return value == other
        }

        return false
    }

    fun getBit(pos: Int): Boolean {
        return value.getBit(pos)
    }

    override fun hashCode(): Int {
        var result = address
        result = 31 * result + value
        return result
    }
}