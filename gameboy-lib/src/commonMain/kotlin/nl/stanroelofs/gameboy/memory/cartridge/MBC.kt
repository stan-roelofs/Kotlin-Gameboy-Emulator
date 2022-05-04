package nl.stanroelofs.gameboy.memory.cartridge

import nl.stanroelofs.gameboy.utils.Buffer

interface MBC : CartridgeType {
    /** Current ROM bank number */
    var currentRomBank: Int

    /** Current RAM bank number */
    var currentRamBank: Int

    /** Enables or disables RAM */
    var ramEnabled: Boolean

    override fun saveRam(destination: Buffer<Byte>) {
        if (ram == null) {
            throw IllegalStateException("Cannot load save on a cartridge that does not have RAM")
        }

        if (!hasBattery) {
            throw IllegalStateException("Cannot save on MBC without battery")
        }

        val bankSize = ram!![0].size
        val ramSize = ram!!.size * bankSize
        for (i in 0 until ramSize) {
            val bank = i / bankSize // Calculate current bank
            val index = i - (bank * bankSize) // Calculate index of current byte

            destination.put(ram!![bank][index].toByte())
        }
    }

    override fun loadRam(source: Buffer<Byte>) {
        try {
            if (ram == null) {
                throw IllegalStateException("Cannot load save on a cartridge that does not have RAM")
            }

            if (!hasBattery) {
                throw IllegalStateException("Cannot load on MBC without battery")
            }

            // Calculate total size of ram by multiplying the number of banks by the bank size
            val bankSize = ram!![0].size
            val ramSize = ram!!.size * bankSize

            if (ramSize != source.length()) {
                throw IllegalArgumentException("Size does not match cartridge ram size: ${source.length()} $ramSize")
            }

            // Load data into ram
            for (i in 0 until source.length()) {
                val bank: Int = i / bankSize // Calculate current bank
                val index: Int = i - (bank * bankSize) // Calculate index of current byte

                ram!![bank][index] = (source.get(i).toInt()) and 0xFF
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}