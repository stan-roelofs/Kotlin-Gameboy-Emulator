package gameboy.memory.io

import gameboy.memory.Memory
import gameboy.memory.Mmu
import gameboy.utils.toHexString

class Dma(private val mmu: Mmu) : Memory {

    private var requested = false
    private var starting = false
    var inProgress = false
    private val totalBytes = 160
    private var source = 0
    private var currentOffset = 0
    private var lastReadByte = 0
    private var count = 0

    private var DMA = 0

    init {
        reset()
    }

    override fun reset() {
        source = 0
        currentOffset = 0
        inProgress = false
        DMA = 0
        lastReadByte = 0
        starting = false
        requested = false
        count = 0
    }

    fun tick(cycles: Int) {
        count += cycles
        if (count != 8)
            return

        count = 0

        if (inProgress || starting) {
            val currentByte = currentOffset

            // Start writing after 1 setup cycle
            if (currentByte > 0) {
                val targetAddress = 0xFE00 + currentByte - 1
                mmu.dmaWriteByte(targetAddress, lastReadByte)
            }

            val sourceAddress = source + currentByte
            lastReadByte = mmu.dmaReadByte(sourceAddress)
            currentOffset++

            if (currentByte >= totalBytes) {
                inProgress = false
            }
        }
        if (starting) {
            inProgress = true
            starting = false
        }
        if (requested) {
            startTransfer(this.DMA)
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
        lastReadByte = 0
        starting = true
        count = 0

        val newVal = if (value >= 0xf0) value - 0x20 else value
        source = (newVal and 0xFF) * 0x100
    }

    fun getOamAccessible(): Boolean {
        return !inProgress
    }
}