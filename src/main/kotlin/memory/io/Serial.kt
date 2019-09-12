package memory.io

import memory.Memory
import memory.Mmu
import utils.toHexString

class Serial : Memory {

    private var SB = 0
    private var SC = 0

    var testOutput = false

    override fun reset() {
        SB = 0
        SC = 0
    }

    override fun readByte(address: Int): Int {
        return when(address) {
            Mmu.SB -> this.SB
            Mmu.SC -> this.SC or 0b01111110
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Serial")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF

        if (address == 0xFF02 && value == 0x81) {
            testOutput = true
        }

        when(address) {
            Mmu.SB -> this.SB = newVal
            Mmu.SC -> this.SC = newVal
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Serial")
        }
    }

}