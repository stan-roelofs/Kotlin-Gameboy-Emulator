package memory.io

import memory.Memory
import memory.Mmu
import utils.toHexString

class Dma : Memory {

    private var requested = false
    private var starting = false
    private var setInprogress = false

    var inProgress = false
    private val totalBytes = 160
    private var source = 0
    private var delay = 0
    private var currentOffset = 0

    private var DMA = 0

    override fun reset() {
        requested = false
        starting = false
        source = 0
        delay = 0
        currentOffset = 0
        inProgress = false
        DMA = 0
        setInprogress = false
    }

    fun tick(cycles: Int) {
        if (setInprogress) {
            inProgress = true
            setInprogress = false
        }
        if (inProgress) {
            val mmu = Mmu.instance

            val currentByte = currentOffset
            currentOffset++

            if (currentOffset >= totalBytes) {
                inProgress = false
            }

            val targetAddress = 0xFE00 + currentByte
            val sourceAddress = source + currentByte
            mmu.dmaWriteByte(targetAddress, mmu.dmaReadByte(sourceAddress))
        }

        if (starting) {
            startTransfer(this.DMA)
        }

        if (requested) {
            starting = true
            requested = false
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
        requested = true
    }

    private fun startTransfer(value: Int) {
        currentOffset = 0
        setInprogress = true
        starting = false

        val newVal = if (value >= 0xf0) value - 0x20 else value
        source = (newVal and 0xFF) * 0x100
    }
}