package gameboy.memory.cartridge

import Logging
import gameboy.memory.Memory
import java.io.File
import java.nio.file.Files

class Cartridge(file: File) : Memory {

    var type: CartridgeType? = null
    var isSgb = false
    var isGbc = false
    lateinit var title: String
    lateinit var cartridgeFile: File
    var graphicBytesMatch = false
    var licensee = ""
    var destination = ""
    var destinationCode = 0
    var versionNumber = 0
    var oldLicenseeCode = 0
    var headerChecksum = false

    private val logger = Logging.getLogger(Cartridge::class.java.name)

    init {
        reset()
        loadRom(file)
    }

    override fun reset() {
        isSgb = false
        isGbc = false
        graphicBytesMatch = false
        licensee = ""
        destinationCode = 0
        destination = ""
        versionNumber = 0
        oldLicenseeCode = 0
        headerChecksum = false
        type?.reset()
    }

    private fun loadRom(file: File) {
        logger.i("Loading rom: ${file.name}")

        try {
            val data = Files.readAllBytes(file.toPath())
            loadHeader(data)

            type!!.loadRom(data)
            cartridgeFile = file
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadHeader(data: ByteArray) {
        val nintendoGraphicBytes = arrayOf(
                0xCE, 0xED, 0x66, 0x66, 0xCC, 0x0D, 0x00, 0x0B, 0x03, 0x73, 0x00, 0x83, 0x00, 0x0C, 0x00, 0x0D,
                0x00, 0x08, 0x11, 0x1F, 0x88, 0x89, 0x00, 0x0E, 0xDC, 0xCC, 0x6E, 0xE6, 0xDD, 0xDD, 0xD9, 0x99,
                0xBB, 0xBB, 0x67, 0x63, 0x6E, 0x0E, 0xEC, 0xCC, 0xDD, 0xDC, 0x99, 0x9F, 0xBB, 0xB9, 0x33, 0x3E
                )

        val romGraphicBytes = data.copyOfRange(0x104, 0x134)

        graphicBytesMatch = (romGraphicBytes.indices).none { nintendoGraphicBytes[it].toByte() != romGraphicBytes[it] }
        if (!graphicBytesMatch) {
            logger.w("Scrolling Nintendo Graphic bytes do not match, rom would not run on GameBoy")
        } else {
            logger.i("Scrolling Nintendo Graphic bytes match")
        }

        if (data[0x143] == 0x80.toByte()) {
            logger.i("Game supports CGB functions")
            isGbc = true
        }

        if (data[0x143] == 0xC0.toByte()) {
            logger.i("Game requires CGB functions")
            isGbc = true
        }

        title = String(data.copyOfRange(0x134, 0x143), Charsets.US_ASCII)
        logger.i("Title: $title")

        licensee = String(data.copyOfRange(0x144, 0x146), Charsets.US_ASCII)
        logger.i("New Licensee Code: $licensee")

        isSgb = data[0x146] == 0x03.toByte()
        logger.i("Supports SGB functions: $isSgb")

        val romBanks = when(data[0x148].toUByte().toInt()) {
            0x00 -> 2
            0x01 -> 4
            0x02 -> 8
            0x03 -> 16
            0x04 -> 32
            0x05 -> 64
            0x06 -> 128
            0x07 -> 256
            0x08 -> 512
            0x52 -> 72
            0x53 -> 80
            0x54 -> 96
            else -> throw Exception("Invalid ROM size identifier")
        }
        logger.i("Number of ROM banks: $romBanks")

        val ramSize = when(data[0x149].toUByte().toInt()) {
            0x00 -> 0
            0x01 -> 2048
            0x02 -> 8192
            0x03 -> 32768
            0x04 -> 131072
            0x05 -> 65536
            else -> throw Exception("Invalid RAM size identifier")
        }
        logger.i("RAM size: $ramSize bytes")

        when (data[0x147].toUByte().toInt()) {
            0x00 -> {
                logger.i("ROM ONLY")
                type = ROMONLY()
            }
            0x01 -> {
                logger.i("ROM+MBC1")
                type = MBC1(romBanks, 0)
            }
            0x02 -> {
                logger.i("ROM+MBC1+RAM")
                type = MBC1(romBanks, ramSize)
            }
            0x03 -> {
                logger.i("ROM+MBC1+RAM+BATTERY")
                type = MBC1(romBanks, ramSize, true)
            }
            0x05 -> {
                logger.i("ROM+MBC2")
                type = MBC2(romBanks)
            }
            0x06 -> {
                logger.i("ROM+MBC2+BATTERY")
                type = MBC2(romBanks, true)
            }
            0x08 -> logger.i("ROM+RAM")
            0x09 -> logger.i("ROM+RAM+BATTERY")
            0x0B -> logger.i("ROM+MMM01")
            0x0C -> logger.i("ROM+MMM01+RAM")
            0x0D -> logger.i("ROM+MMM01+RAM+BATTERY")
            0x0F -> {
                logger.i("ROM+MBC3+TIMER+BATTERY")
                type = MBC3(romBanks, 0, hasBattery = true, hasTimer = true)
            }
            0x10 -> {
                logger.i("ROM+MBC3+TIMER+RAM+BATTERY")
                type = MBC3(romBanks, ramSize, hasBattery = true, hasTimer = true)
            }
            0x11 -> {
                logger.i("ROM+MBC3")
                type = MBC3(romBanks, 0, hasBattery = false, hasTimer = false)
            }
            0x12 -> {
                logger.i("ROM+MBC3+RAM")
                type = MBC3(romBanks, ramSize, hasBattery = false, hasTimer = false)
            }
            0x13 -> {
                logger.i("ROM+MBC3+RAM+BATTERY")
                type = MBC3(romBanks, ramSize, hasBattery = true, hasTimer = false)
            }
            0x19 -> {
                logger.i("ROM+MBC5")
                type = MBC5(romBanks, 0, false)
            }
            0x1A -> {
                logger.i("ROM+MBC5+RAM")
                type = MBC5(romBanks, ramSize, false)
            }
            0x1B -> {
                logger.i("ROM+MBC5+RAM+BATTERY")
                type = MBC5(romBanks, ramSize, true)
            }
            0x1C -> logger.i("ROM+MBC5+RUMBLE")
            0x1D -> logger.i("ROM+MBC5+RUMBLE+SRAM")
            0x1E -> logger.i("ROM+MBC5+RUMBLE+SRAM+BATTERY")
            0x1F -> logger.i("Pocket Camera")
            0xFD -> logger.i("Bandai TAMA5")
            0xFE -> logger.i("Hudson HuC-3")
            0xFF -> logger.i("Hudson HuC-1")
        }

        destinationCode = data[0x14A].toUByte().toInt()
        destination = if (destinationCode == 0x00) "Japanese" else "Non-Japanese"
        logger.i("Destination Code: $destination")

        oldLicenseeCode = data[0x14B].toUByte().toInt()
        if (isSgb) {
            logger.w("SGB Functions require old licensee code to be 0x33")
        }
        logger.i("Old Licensee Code: $oldLicenseeCode")

        versionNumber = data[0x14C].toUByte().toInt()
        logger.i("ROM version: $versionNumber")

        var sum = 0
        for (i in 0x134..0x14C) {
            sum = sum - data[i] - 1
        }
        sum = sum and 0xFF
        val romHeaderChecksum = data[0x14D].toInt() and 0xFF

        if (sum != romHeaderChecksum) {
            headerChecksum = false
            logger.w("Header Checksum is incorrect, game would normally not run")
        } else {
            headerChecksum = true
            logger.i("Header Checksum correct")
        }

        //val romChecksum = Arrays.copyOfRange(data, 0x14E, 0x14F)
    }

    override fun readByte(address: Int): Int {
        return type!!.readByte(address)
    }

    override fun writeByte(address: Int, value: Int) {
        return type!!.writeByte(address, value and 0xFF)
    }

    fun saveRam(file: File) {
        type!!.saveRam(file)
    }

    fun loadRam(file: File) {
        type!!.loadRam(file)
    }
}