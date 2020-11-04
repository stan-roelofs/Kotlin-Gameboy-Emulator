package gameboy.memory.io

import gameboy.memory.Memory
import gameboy.memory.Mmu
import gameboy.utils.Log
import gameboy.utils.getBit
import gameboy.utils.setSecondByte
import gameboy.utils.toHexString

class Hdma(private val mmu: Mmu) : Memory {

    private var hdma1 = 0
    private var hdma2 = 0
    private var hdma3 = 0
    private var hdma4 = 0
    private var length = 0
    private var hblankTransfer = false
    private var inProgress = false

    private var destination = 0
    private var source = 0

    init {
        reset()
    }

    override fun reset() {
        hdma1 = 0
        hdma2 = 0
        hdma3 = 0
        hdma4 = 0
        length  = 0
        hblankTransfer = false
        inProgress = false
        destination = 0
        source = 0
    }

    fun tick(cycles: Int, hblank: Boolean, lcdEnabled: Boolean) {
        if (!inProgress)
            return

        if (hblankTransfer && (!hblank && lcdEnabled))
            return

        for (i in 0 until 0x10) {
            mmu.writeByte(destination + i, mmu.readByte(source + i))
        }

        source += 0x10
        destination += 0x10

        if (length-- == 0) {
            inProgress = false
            length = 0x7F
        }
    }

    override fun readByte(address: Int): Int {
        return when (address) {
            Mmu.HDMA1 -> hdma1
            Mmu.HDMA2 -> hdma2
            Mmu.HDMA3 -> hdma3
            Mmu.HDMA4 -> hdma4
            Mmu.HDMA5 -> {
                val hblank = if (hblankTransfer) 1 else 0
                length or (hblank shl 7)
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Dma")
        }
    }

    override fun writeByte(address: Int, value: Int) {
        val newVal = value and 0xFF
        when (address) {
            Mmu.HDMA1 -> hdma1 = newVal
            Mmu.HDMA2 -> hdma2 = newVal
            Mmu.HDMA3 -> hdma3 = newVal
            Mmu.HDMA4 -> hdma4 = newVal
            Mmu.HDMA5 -> {
                val hblankMode = newVal.getBit(7)
                if (inProgress && !hblankMode)
                    stopTransfer()
                else
                    startTransfer(newVal)
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Dma")
        }
    }

    private fun startTransfer(value: Int) {
        hblankTransfer = value.getBit(7)
        length = value and 0b01111111

        val sourceHigh = hdma1
        val sourceLow = hdma2 and 0b11110000 // The lower four bits are ignored
        source = setSecondByte(sourceLow, sourceHigh)

        val destinationHigh = (hdma3 or 0b10000000) and 0b00011111 // Only bits 12-4 are respected
        val destinationLow = hdma4 and 0b11110000
        destination = setSecondByte(destinationLow, destinationHigh)

        inProgress = true

        Log.d("Starting HDMA transfer from ")
    }

    private fun stopTransfer() {
        inProgress = false
    }
}