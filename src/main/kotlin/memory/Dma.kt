package memory

import utils.toHexString

class Dma : Memory {

    private var counter = 0
    private var transfer = false
    private val cyclesMax = 648
    private var source = 0

    private var DMA = 0

    override fun reset() {
        counter = 0
        transfer = false
        DMA = 0
    }

    fun tick(cycles: Int) {
        if (transfer) {
            /*
            val mmu = Mmu.instance

            var bytes = cycles / 4
            while (counter <= cyclesMax && bytes > 0) {
                if (counter == 0) {
                    counter += 4
                    break
                }

                val offset = (counter / 4) - 4
                mmu.writeByte(0xFF + offset, mmu.readByte(source + offset))
                counter += 4
                bytes--
            }*/

            counter += cycles
            if (counter >= cyclesMax) {
                counter = 0
                transfer = false

                val mmu = Mmu.instance
                for (i in 0 until 160) {
                    mmu.writeByte(0xFE00 + i, mmu.readByte(source + i))
                }
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
        transfer = true
        source = (value and 0xFF) * 0x100
    }
}