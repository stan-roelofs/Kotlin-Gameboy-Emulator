import java.io.File
import java.nio.file.Files
import java.util.*
import java.util.logging.Logger

class Cartridge(file: File) {
    companion object {
        val logger = Logger.getLogger("Cartridge")!!
    }

    private lateinit var rom: IntArray
    private var ram: IntArray = IntArray(1) //TODO
    var isSgb = false
    var isGbc = false

    init {
        loadRom(file)
    }

    private fun loadRom(file: File) {
        logger.info("Loading rom: ${file.name}")

        try {
            val data = Files.readAllBytes(file.toPath())
            loadHeader(data)

            this.rom = IntArray(data.size)
            for (i in 0 until data.size) {
                rom[i] = (data[i].toInt()) and 0xFF
            }

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

        val romGraphicBytes = Arrays.copyOfRange(data, 0x104, 0x134)

        val equal = (0 until romGraphicBytes.size).none { nintendoGraphicBytes[it].toByte() != romGraphicBytes[it] }
        if (!equal) {
            logger.warning("Scrolling Nintendo Graphic bytes do not match, rom would not run on GameBoy")
        } else {
            logger.info("Scrolling Nintendo Graphic bytes match")
        }

        val romColorGB = data[0x143].toInt() == 0x80
        isGbc = romColorGB
        logger.info("Is Color GB: $romColorGB")

        val romTitle = String(Arrays.copyOfRange(data, 0x134, 0x143), Charsets.US_ASCII)
        logger.info("Title: $romTitle")

        val romLicensee = String(Arrays.copyOfRange(data, 0x144, 0x146), Charsets.US_ASCII)
        logger.info("Licensee Code: $romLicensee")

        val romSGB = data[0x146].toInt() == 0x03
        isSgb = romSGB
        logger.info("Is SGB: $romSGB")

        val romType = data[0x147].toInt()
        when (romType) {
            0x00 -> logger.info("ROM ONLY")
            0x01 -> logger.info("ROM+MBC1")
            0x02 -> logger.info("ROM+MBC1+RAM")
            0x03 -> logger.info("ROM+MBC1+RAM+BATTERY")
            0x05 -> logger.info("ROM+MBC2")
            0x06 -> logger.info("ROM+MBC2+BATTERY")
            0x08 -> logger.info("ROM+RAM")
            0x09 -> logger.info("ROM+RAM+BATTERY")
            0x0B -> logger.info("ROM+MMM01")
            0x0C -> logger.info("ROM+MMM01+RAM")
            0x0D -> logger.info("ROM+MMM01+RAM+BATTERY")
            0x0F -> logger.info("ROM+MBC3+TIMER+BATTERY")
            0x10 -> logger.info("ROM+MBC3+TIMER+RAM+BATTERY")
            0x11 -> logger.info("ROM+MBC3")
            0x12 -> logger.info("ROM+MBC3+RAM")
            0x13 -> logger.info("ROM+MBC3+RAM+BATTERY")
            0x19 -> logger.info("ROM+MBC5")
            0x1A -> logger.info("ROM+MBC5+RAM")
            0x1B -> logger.info("ROM+MBC5+RAM+BATTERY")
            0x1C -> logger.info("ROM+MBC5+RUMBLE")
            0x1D -> logger.info("ROM+MBC5+RUMBLE+SRAM")
            0x1E -> logger.info("ROM+MBC5+RUMBLE+SRAM+BATTERY")
            0x1F -> logger.info("Pocket Camera")
            0xFD -> logger.info("Bandai TAMA5")
            0xFE -> logger.info("Hudson HuC-3")
            0xFF -> logger.info("Hudson HuC-1")
        }

        val romSize = data[0x148].toInt()
        val romBanks = when(romSize) {
            0x00 -> 2
            0x01 -> 4
            0x02 -> 8
            0x03 -> 16
            0x04 -> 32
            0x05 -> 64
            0x06 -> 128
            0x52 -> 72
            0x53 -> 80
            0x54 -> 96
            else -> throw Exception("Invalid ROM size identifier")
        }
        logger.info("Number of ROM banks: $romBanks")

        val ramSizeByte = data[0x149].toInt()
        val ramSize = when(ramSizeByte) {
            0x00 -> 0
            0x01 -> 2048
            0x02 -> 8192
            0x03 -> 32768
            else -> throw Exception("Invalid RAM size identifier")
        }
        logger.info("RAM size: $ramSize bytes")

        val romDestCode = data[0x14A].toInt()
        val romDest = if (romDestCode == 0x00) "Japanese" else "Non-Japanese"
        logger.info("Destination Code: $romDest")

        //val romOldLicensee = data[0x14B].toInt()
        //val romVersionNumber = data[0x14C].toInt()
        //val romHeaderChecksum = data[0x14D].toInt()
        //val romChecksum = Arrays.copyOfRange(data, 0x14E, 0x14F)
    }

    fun readRom(address: Int): Int {
        return this.rom[address]
    }

    fun readRam(address: Int): Int {
        return 0
    }

    fun writeRam(address: Int, value: Int) {
        this.ram[address] = value
    }
}