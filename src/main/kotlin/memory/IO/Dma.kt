package memory.IO

import memory.Memory
import memory.Mmu
import utils.toHexString

class Dma : Memory {

    private var counter = 0
    var inProgress = false
    private val cyclesMax = 644
    private var source = 0

    private var DMA = 0

    override fun reset() {
        counter = 0
        inProgress = false
        DMA = 0
    }

    fun tick(cycles: Int) {
        if (inProgress) {
            val mmu = Mmu.instance

            var bytes = cycles / 4
            while (counter < cyclesMax && bytes > 0) {
                if (counter == 0) {
                    counter += 4

                    if (bytes == 1) {
                        break
                    }
                }

                val offset = ((counter - 4) / 4)
                mmu.writeByte(0xFE00 + offset, mmu.readByte(source + offset))
                counter += 4
                bytes--
            }

            if (counter >= cyclesMax) {
                inProgress = false
            }
        }
    }

    override fun readByte(address: Int): Int {
        if (address != Mmu.DMA) {
            throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Dma")
        }
        return this.DMA
    }

    override fun writeByte(address: Int, value: Int) {
        if (address != Mmu.DMA) {
            throw IllegalArgumentException("Address ${address.toHexString()} does not belong to Dma")
        }

        this.DMA = value and 0xFF
        startTransfer(value)
    }

    private fun startTransfer(value: Int) {
        counter = 0
        inProgress = true

        val newVal = if (value >= 0xf0) value - 0x20 else value
        source = (newVal and 0xFF) * 0x100
    }
}