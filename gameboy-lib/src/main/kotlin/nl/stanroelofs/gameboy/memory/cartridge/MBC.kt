package nl.stanroelofs.gameboy.memory.cartridge

import java.io.InputStream
import java.io.OutputStream

interface MBC : CartridgeType {
    /** Current ROM bank number */
    var currentRomBank: Int

    /** Current RAM bank number */
    var currentRamBank: Int

    /** Enables or disables RAM */
    var ramEnabled: Boolean

    override fun saveRam(destination: OutputStream) {
        if (ram == null) {
            throw IllegalStateException("Cannot load save on a cartridge that does not have RAM")
        }

        if (!hasBattery) {
            throw IllegalStateException("Cannot save on MBC without battery")
        }

        val bankSize = ram!![0].size
        val ramSize = ram!!.size * bankSize
        // TODO
        for (i in 0 until ramSize) {
            val bank = i / bankSize // Calculate current bank
            val index = i - (bank * bankSize) // Calculate index of current byte

           // destination.write(ram!![bank][index])
        }
    }

    override fun loadRam(source: InputStream) {
        // TODO
//        try {
//            if (ram == null) {
//                throw IllegalStateException("Cannot load save on a cartridge that does not have RAM")
//            }
//
//            if (!hasBattery) {
//                throw IllegalStateException("Cannot load on MBC without battery")
//            }
//
//            val data = Files.readAllBytes(file.toPath())
//
//            // Calculate total size of ram by multiplying the number of banks by the bank size
//            val bankSize = ram!![0].size
//            val ramSize = ram!!.size * bankSize
//
//            if (ramSize != data.size) {
//                throw IllegalArgumentException("Size does not match cartridge ram size: ${data.size} $ramSize")
//            }
//
//            // Load data into ram
//            for (i in 0 until data.size) {
//                val bank: Int = i / bankSize // Calculate current bank
//                val index: Int = i - (bank * bankSize) // Calculate index of current byte
//
//                ram!![bank][index] = (data[i].toInt()) and 0xFF
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }
}