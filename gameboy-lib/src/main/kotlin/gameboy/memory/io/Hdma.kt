package gameboy.memory.io

import gameboy.memory.Memory
import gameboy.memory.Mmu
import gameboy.utils.Log
import gameboy.utils.getBit
import gameboy.utils.setSecondByte
import gameboy.utils.toHexString

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

    private var destination = 0
    private var source = 0

    private var count = 0
    var state = State.FINISHED
        private set

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
    }

    fun tick(cycles: Int, hblank: Boolean, lcdEnabled: Boolean) {
        count += cycles
        if (count >= 2)
            count = 0
        else
            return

        when(state) {
            State.FINISHED -> {}
            State.TRANSFER -> {
                mmu.writeByte(destination, mmu.readByte(source))
                source++
                destination++
                length--

                if (length == 0) {
                    state = State.FINISHED
                    hdma5 = 0xFF
                    return
                }

                if (hblankTransfer && length % 0x10 == 0)
                    state = State.WAIT_HBLANK
            }
            State.WAIT_HBLANK -> {
                if (hblank || !lcdEnabled)
                    state = State.TRANSFER
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
                hdma5 = newVal

                if (state == State.FINISHED) {
                    startTransfer(newVal)
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
        source = setSecondByte(sourceLow, sourceHigh)

        val destinationHigh = hdma3 and 0b00011111 // Only bits 12-4 are respected
        val destinationLow = hdma4 and 0b11110000
        destination = setSecondByte(destinationLow, destinationHigh)
        destination += 0x8000

        if (destination + length >= 0x10000) {
            length = 0x10000 - destination
        }

        state = if (hblankTransfer)
            State.WAIT_HBLANK
        else
            State.TRANSFER

        Log.d("Starting ${if (hblankTransfer) "G" else "H"}DMA transfer from ${source.toHexString(4)} to ${destination.toHexString(4)}")
    }
}
