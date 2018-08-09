package memory

import getBit
import setBit

class Lcd : Memory {

    private var LCDC = 0
    private var LY = 0
    private var LYC = 0
    private var STAT = 0
    private var SCY = 0
    private var SCX = 0
    private var WY = 0
    private var WX = 0
    private var BGP = 0
    private var OBP0 = 0
    private var OBP1 = 0

    override fun reset() {
        LCDC = 0x91
        LY = 0x00
        LYC = 0x00
        STAT = 0x00
        SCY = 0x00
        SCX = 0x00
        WY = 0x00
        WX = 0x00
        BGP = 0xFC
        OBP0 = 0xFF
        OBP1 = 0xFF
    }

    fun tick(cyclesElapsed: Int) {

    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.LCDC -> this.LCDC
            Mmu.LY -> {
                if (LCDC.getBit(7)) {
                    this.LY
                } else {
                    0
                }
            }
            Mmu.LYC -> this.LYC
            Mmu.STAT -> this.STAT
            Mmu.SCY -> this.SCY
            Mmu.SCX -> this.SCX
            Mmu.WY -> this.WY
            Mmu.WX -> this.WX
            Mmu.BGP -> this.BGP
            Mmu.OBP0 -> this.OBP0
            Mmu.OBP1 -> this.OBP1
            else -> throw IllegalArgumentException("Address $address does not belong to Lcd")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when(address) {
            Mmu.LCDC -> this.LCDC = newVal
            Mmu.LY -> this.LY = 0
            Mmu.LYC -> this.LYC = newVal
            Mmu.STAT -> this.STAT = newVal
            Mmu.SCY -> this.SCY = newVal
            Mmu.SCX -> this.SCX = newVal
            Mmu.WY -> this.WY = newVal
            Mmu.WX -> this.WX = newVal
            Mmu.BGP -> this.BGP = newVal
            Mmu.OBP0 -> this.OBP0 = newVal
            Mmu.OBP1 -> this.OBP1 = newVal
            else -> throw IllegalArgumentException("Address $address does not belong to Lcd")
        }
    }
}