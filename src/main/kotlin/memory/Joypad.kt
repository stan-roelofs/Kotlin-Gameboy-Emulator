package memory

import utils.clearBit
import utils.getBit
import java.util.*

class Joypad : Memory {

    private var P1 = 0
    private val pressedKeys = EnumSet.noneOf(JoypadKey::class.java)!!

    override fun reset() {
        P1 = 0
        pressedKeys.clear()
    }

    override fun readByte(address: Int): Int {
        if (address != Mmu.P1) {
            throw IllegalArgumentException("Address $address does not belong to Joypad")
        }
        return this.P1
    }

    override fun writeByte(address: Int, value: Int) {
        if (address != Mmu.P1) {
            throw IllegalArgumentException("Address $address does not belong to Joypad")
        }
        this.P1 = value and 0xFF
        updateData()
    }

    private fun updateData() {
        var newVal = P1 and 0b00110000 or 0b11001111
        for (key in pressedKeys) {
            if (!newVal.getBit(key.selectBit)) {
                newVal = clearBit(newVal, key.bit)
            }
        }
        P1 = newVal
    }

    fun keyPressed(key: JoypadKey) {
        pressedKeys.add(key)
        updateData()
        if (P1.getBit(key.selectBit)) {
            requestInterrupt(4)
        }
    }

    fun keyReleased(key: JoypadKey) {
        val removed = pressedKeys.remove(key)
        if (!removed) {
            return
        }
        updateData()
    }

    enum class JoypadKey(val selectBit: Int, val bit: Int) {
        LEFT(4, 1), RIGHT(4, 0), UP(4, 2), DOWN(4, 3),
        A(5, 0), B(5, 1), START(5, 3), SELECT(5, 2)
    }
}