package memory

import utils.setSecondByte

class Dma : Memory {

    private var counter = 0
    private var transfer = false
    private val cyclesMax = 160 * 4 + 4
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
                val mmu = Mmu.instance
                counter = 0
                transfer = false

                for (i in 0 until 160) {
                    mmu.writeByte(0xFE00 + i, mmu.readByte(source + i))
                }
            }
        }
    }

    override fun readByte(address: Int): Int {
        return DMA
    }

    override fun writeByte(address: Int, value: Int) {
        DMA = value and 0xFF
        startTransfer()
    }

    private fun startTransfer() {
        counter = 0
        transfer = true
        source = setSecondByte(source, DMA)
    }
}