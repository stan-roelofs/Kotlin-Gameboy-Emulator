package blargg

import MAX_ITERATIONS
import getScreenHash
import makeScreenshot
import mooneye.MooneyeTest
import nl.stanroelofs.gameboy.GameBoy
import nl.stanroelofs.gameboy.GameBoyDMG
import nl.stanroelofs.gameboy.memory.io.graphics.VSyncListener
import nl.stanroelofs.minilog.Logging
import java.io.File
import java.net.URI
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

abstract class BlarggTestSerial : BlarggTest {

    private val logger = Logging.get(BlarggTestSerial::class.java.name)

    override fun runBlarggTest(fileName: String) {
        val url: URI = BlarggTest::class.java.classLoader.getResource("blargg/$path/$fileName")!!.toURI()
        val romFile = File(url)
        val gb = GameBoyDMG()
        gb.cartridge.load(romFile.readBytes())

        logger.i{""}
        logger.i{"Running Blargg Test: $fileName"}
        logger.i{"Using serial output to validate"}

        var output = ""
        for (i in 0..MAX_ITERATIONS) {
            gb.step()
            if (gb.mmu.io.serial.testOutput) {
                val char = gb.mmu.readByte(0xFF01).toChar()
                output += char
                gb.mmu.io.serial.testOutput = false
            }
        }
        logger.i{"Finished test with output: $output"}

        val result = output.lowercase().contains("passed")
        assertEquals(true, result)
        return
    }
}

abstract class BlarggTestMemory : BlarggTest {

    private val logger = Logging.get(BlarggTestMemory::class.java.name)

    override fun runBlarggTest(fileName: String) {
        val url: URI = BlarggTest::class.java.classLoader.getResource("blargg/$path/$fileName")!!.toURI()
        val romFile = File(url)
        val gb = GameBoyDMG()
        gb.cartridge.load(romFile.readBytes())

        logger.i{""}
        logger.i{"Running Blargg Test: $fileName"}

         /*
         * 0xA000 holds the test status code. While the test is running it is set to 0x80.
         * 0xA001-0xA003 indicate that the data is actually from a test and not random and should equal 0xDE, 0xB0, 0x61
         * Text output is appended to 0xA004, terminated by 0
         */
        logger.i{"Using memory at A000 to validate"}

        var i = 0
        var status = false
        while ((!status || gb.mmu.readByte(0xA000) == 0x80) && i < MAX_ITERATIONS) {
            gb.step()
            i++

            if (!status && gb.mmu.readByte(0xA000) == 0x80) {
                status = true
            }
        }

        if (i == MAX_ITERATIONS) {
            logger.w{"Max iterations reached"}
        }

        logger.i{"Test finished after $i iterations"}

        assertEquals(0xDE, gb.mmu.readByte(0xA001))
        assertEquals(0xB0, gb.mmu.readByte(0xA002))
        assertEquals(0x61, gb.mmu.readByte(0xA003))

        val statusCode = gb.mmu.readByte(0xA000)

        var output = ""
        var currentAddress = 0
        while (gb.mmu.readByte(0xA004 + currentAddress) != 0) {
            val char = gb.mmu.readByte(0xA004 + currentAddress).toChar()
            output += char
            currentAddress++
        }

        logger.i{"Finished test with status code: $statusCode and output: $output"}

        assertEquals(0, statusCode)
    }
}

abstract class BlarggTestScreenhash : BlarggTest, VSyncListener {

    private val logger = Logging.get(BlarggTestScreenhash::class.java.name)
    private val lastBuffer = ByteArray(GameBoy.SCREEN_HEIGHT * GameBoy.SCREEN_WIDTH * 3)

    override fun runBlarggTest(fileName: String) {
        logger.i{""}
        logger.i{"Running Blargg Test: $fileName"}
        logger.i{"Using screen hash to validate"}

        val inputHashURI = MooneyeTest::class.java.classLoader.getResource("testhashes/blargg/$path/$fileName.txt")?.toURI()
        var inputHash = 0

        if (inputHashURI == null)
            logger.e{"No input hash, test will fail regardless of output"}
        else {
            val inputFile = File(inputHashURI)
            assertTrue(inputFile.exists())
            inputHash = inputFile.readText().toInt()
            logger.i{"Provided hash: $inputHash"}
        }

        val romURI = MooneyeTest::class.java.classLoader.getResource("blargg/$path/$fileName")?.toURI()
        assertNotNull(romURI)
        val romFile = File(romURI)
        assertTrue(romFile.exists())

        val testOutputScreenshots = File("testoutput/screenshots/blargg/$path")
        val testOutputHashes = File("testoutput/hashes/blargg/$path")
        val testOutputHash = File("$testOutputHashes/$fileName.txt")
        val testOutputScreenshot = File("$testOutputScreenshots/$fileName.png")

        testOutputScreenshots.mkdirs()
        testOutputHashes.mkdirs()
        testOutputHash.createNewFile()

        assertTrue(testOutputHash.exists())

        val gb = GameBoyDMG()
        gb.cartridge.load(romFile.readBytes())
        gb.mmu.io.ppu.lcd.addListener(this)

        for (i in 0..MAX_ITERATIONS) {
            gb.step()
        }

        val hash = getScreenHash(lastBuffer)
        makeScreenshot(testOutputScreenshot, lastBuffer)
        testOutputHash.writeText("$hash")

        logger.i{"Hash: $hash"}
        assertNotNull(inputHashURI)
        assertEquals(inputHash, hash)
    }

    override fun vsync(screenBuffer: ByteArray) {
        screenBuffer.copyInto(lastBuffer)
    }
}

interface BlarggTest {
    val path: String

    fun runBlarggTest(fileName: String)
}