package nl.stanroelofs.gameboy.memory.io

import nl.stanroelofs.gameboy.cpu.Interrupt
import nl.stanroelofs.gameboy.memory.Memory
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.clearBit
import nl.stanroelofs.gameboy.utils.getBit
import nl.stanroelofs.gameboy.utils.toHexString
import java.util.*

class Joypad(private val mmu: Mmu) : Memory {

    private var P1 = 0
    private val pressedKeys = EnumSet.noneOf(JoypadKey::class.java)!!

    init {
        reset()
    }

    override fun reset() {
        P1 = 0b11001111
        pressedKeys.clear()
    }

    override fun readByte(address: Int): Int {
        if (address != Mmu.P1) {
            throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Joypad")
        }
        return this.P1
    }

    override fun writeByte(address: Int, value: Int) {
        if (address != Mmu.P1) {
            throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Joypad")
        }
        this.P1 = value and 0xFF
        updateData()
    }

    private fun updateData() {
        var newVal = P1 and 0b00110000 or 0b11001111
        for (key in pressedKeys) {
            if (!newVal.getBit(key.selectBit)) {
                newVal = newVal.clearBit(key.bit)
            }
        }
        P1 = newVal
    }

    fun keyPressed(key: JoypadKey) {
        pressedKeys.add(key)
        updateData()
        if (P1.getBit(key.selectBit)) {
            mmu.requestInterrupt(Interrupt.JOYPAD)
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