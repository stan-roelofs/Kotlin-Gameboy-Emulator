package mooneye

import getScreenHash
import makeScreenshot
import nl.stanroelofs.gameboy.GameBoy
import nl.stanroelofs.gameboy.GameBoyCGB
import nl.stanroelofs.gameboy.GameBoyDMG
import nl.stanroelofs.gameboy.memory.cartridge.Cartridge
import nl.stanroelofs.gameboy.memory.io.graphics.VSyncListener
import nl.stanroelofs.minilog.Logging
import org.junit.Assert
import java.io.File

abstract class MooneyeTest : VSyncListener {

    companion object {
        const val DEBUG_INSTRUCTION = 0x40
    }

    enum class Type {
        REGISTERS,
        SCREENHASH
    }

    private val logger = Logging.getLogger(MooneyeTest::class.java.name)

    abstract val path: String
    private val lastBuffer = ByteArray(GameBoy.SCREEN_HEIGHT * GameBoy.SCREEN_WIDTH * 3)

    // Runs one of the Mooneye GB test roms
    // Test passes when a hash of the screenBuffer matches a provided hash
    // Provided hash is a hash of the screenBuffer after a test passed
    fun runMooneyeTest(fileName: String, type: Type = Type.REGISTERS, forceCgb : Boolean = false) {
        val inputHashURI = MooneyeTest::class.java.classLoader.getResource("testhashes/mooneye/$path/$fileName.txt")?.toURI()
        var inputHash = 0

        if (inputHashURI != null) {
            val inputFile = File(inputHashURI)
            Assert.assertTrue(inputFile.exists())
            inputHash = inputFile.readText().toInt()
        }

        val romURI = MooneyeTest::class.java.classLoader.getResource("mooneye/$path/$fileName")?.toURI()
        Assert.assertNotNull(romURI)
        val romFile = File(romURI!!)
        Assert.assertTrue(romFile.exists())

        val testOutputScreenshots = File("testoutput/screenshots/mooneye/$path")
        val testOutputHashes = File("testoutput/hashes/mooneye/$path")
        val testOutputHash = File("$testOutputHashes/$fileName.txt")
        val testOutputScreenshot = File("$testOutputScreenshots/$fileName.png")

        testOutputScreenshots.mkdirs()
        testOutputHashes.mkdirs()
        testOutputHash.createNewFile()

        Assert.assertTrue(testOutputHash.exists())

        val gb = if (forceCgb) GameBoyCGB(Cartridge(romFile)) else GameBoyDMG(Cartridge(romFile))
        gb.mmu.io.ppu.lcd.addListener(this)

        logger.i{""}
        logger.i{"Running Mooneye Test: $fileName"}
        if (type == Type.SCREENHASH)
            logger.i{"Provided hash $inputHash"}

        for (i in 0..50000000) {
            if (gb.cpu.opcode == DEBUG_INSTRUCTION)
                break
            gb.step()
            gb.step()
            gb.step()
            gb.step()
        }

        val hash = getScreenHash(lastBuffer)
        makeScreenshot(testOutputScreenshot, lastBuffer)
        testOutputHash.writeText("$hash")

        if (type == Type.REGISTERS) {
            Assert.assertEquals(0, gb.cpu.registers.A)
            Assert.assertEquals(3, gb.cpu.registers.B)
            Assert.assertEquals(5, gb.cpu.registers.C)
            Assert.assertEquals(8, gb.cpu.registers.D)
            Assert.assertEquals(13, gb.cpu.registers.E)
            Assert.assertEquals(21, gb.cpu.registers.H)
            Assert.assertEquals(34, gb.cpu.registers.L)
        }

        if (type == Type.SCREENHASH) {
            logger.i{"Hash: $hash"}
            Assert.assertNotNull(inputHashURI)
            Assert.assertEquals(inputHash, hash)
        }
    }

    override fun vsync(screenBuffer: ByteArray) {
        screenBuffer.copyInto(lastBuffer)
    }
}