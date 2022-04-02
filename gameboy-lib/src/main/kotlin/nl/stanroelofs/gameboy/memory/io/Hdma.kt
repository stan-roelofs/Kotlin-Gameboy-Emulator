package nl.stanroelofs.gameboy.memory.io

import nl.stanroelofs.gameboy.memory.Memory
import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.utils.getBit
import nl.stanroelofs.gameboy.utils.setSecondByte
import nl.stanroelofs.gameboy.utils.toHexString

class Hdma(private val mmu: Mmu) : Memory {

    enum class State {
        FINISHED,
        TRANSFER,
        WAIT_HBLANK,
    }

    private var hdma1 = 0
    private var hdma2 = 0
    private var hdma3 = 0
    private var hdma4 = 0
    private var hdma5 = 0
    private var length = 0
    private var hblankTransfer = false
    private var previousHblank = false

    private var destination = 0
    private var source = 0

    private var count = 0
    private var state = State.FINISHED

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
        destination = 0
        source = 0
        count = 0
        state = State.FINISHED
        previousHblank = false
    }

    fun tick(hblank: Boolean, lcdEnabled: Boolean) {
        if (!hblank)
            previousHblank = false

        when(state) {
            State.FINISHED -> return
            State.TRANSFER -> {
                ++count
                if (count < 0x20)
                    return

                count = 0

                for (i in 0 until 0x10) {
                    mmu.writeByte(destination + i, mmu.readByte(source + i))
                }
                source += 0x10
                destination += 0x10
                length -= 0x10
                hdma5 = ((length / 0x10) - 1) and 0x1F
                hdma1 = (source shr 8) and 0xFF
                hdma2 = source and 0xFF
                hdma3 = (destination shr 8) and 0xFF
                hdma4 = destination and 0xFF

                if (length == 0) {
                    state = State.FINISHED
                    hdma5 = 0xFF
                    return
                }

                if (hblankTransfer) {
                    state = State.WAIT_HBLANK
                    return
                }
            }
            State.WAIT_HBLANK -> {
                if (!previousHblank && (hblank || !lcdEnabled)) {
                    state = State.TRANSFER
                    previousHblank = true
                    count = 0
                }
            }
        }
    }

    override fun readByte(address: Int): Int {
        return when (address) {
            Mmu.HDMA1 -> 0xFF
            Mmu.HDMA2 -> 0xFF
            Mmu.HDMA3 -> 0xFF
            Mmu.HDMA4 -> 0xFF
            Mmu.HDMA5 -> hdma5
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
                hdma5 = newVal and 0x1F

                if (state == State.FINISHED) {
                    startTransfer(newVal)
                    return
                }

                val hblankMode = newVal.getBit(7)
                if (state != State.FINISHED && hblankTransfer) {
                    if (hblankMode) {
                        // Restart
                        startTransfer(newVal)
                    } else {
                        // stop
                        state = State.FINISHED
                        hdma5 = newVal or 0x80
                    }
                }
            }
            else -> throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Dma")
        }
    }

    private fun startTransfer(value: Int) {
        hblankTransfer = value.getBit(7)
        length = ((value and 0x7F) + 1) * 0x10

        val sourceHigh = hdma1
        val sourceLow = hdma2 and 0b11110000 // The lower four bits are ignored
        source = sourceLow.setSecondByte(sourceHigh)

        val destinationHigh = hdma3 and 0b00011111 // Only bits 12-4 are respected
        val destinationLow = hdma4 and 0b11110000
        destination = destinationLow.setSecondByte(destinationHigh)
        destination += 0x8000

        if (destination + length >= 0x10000) {
            length = 0x10000 - destination
        }

        state = if (hblankTransfer)
            State.TRANSFER
        else
            State.TRANSFER
    }

    fun inProgress() : Boolean {
        return state == State.TRANSFER
    }
}
