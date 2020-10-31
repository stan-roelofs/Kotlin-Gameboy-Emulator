package mooneye

import gameboy.GameBoyDMG
import gameboy.memory.cartridge.Cartridge
import gameboy.utils.Log
import getScreenHash
import makeScreenshot
import org.junit.Assert
import java.io.File

abstract class MooneyeTest {

    abstract val path: String

    // Runs one of the Mooneye GB test roms
    // Test passes when a hash of the screenBuffer matches a provided hash
    // Provided hash is a hash of the screenBuffer after a test passed
    fun runMooneyeTest(fileName: String) {
        val inputHashURI = MooneyeTest::class.java.classLoader.getResource("testhashes/$fileName.txt")?.toURI()
        var inputHash = 0

        if (inputHashURI == null)
            Log.e("No input hash, test will fail regardless of output")
        else {
            val inputFile = File(inputHashURI)
            Assert.assertTrue(inputFile.exists())
            inputHash = inputFile.readText().toInt()
        }

        val romURI = MooneyeTest::class.java.classLoader.getResource("mooneye/$path/$fileName")?.toURI()
        Assert.assertNotNull(romURI)
        val romFile = File(romURI!!)
        Assert.assertTrue(romFile.exists())

        val testOutputScreenshots = File("testoutput/screenshots")
        val testOutputHashes = File("testoutput/hashes")
        val testOutputHash = File("$testOutputHashes/$fileName.txt")
        val testOutputScreenshot = File("$testOutputScreenshots/$fileName.png")

        testOutputScreenshots.mkdirs()
        testOutputHashes.mkdirs()
        testOutputHash.createNewFile()

        Assert.assertTrue(testOutputHash.exists())

        val gb = GameBoyDMG(Cartridge(romFile))

        Log.i("")
        Log.i("Running Mooneye Test: $fileName")
        Log.i("Provided hash $inputHash")

        // TODO: theres probably a better way to do this...
        for (i in 0..10000000) {
            gb.step()
        }

        val hash = getScreenHash(gb.mmu.io.lcd.screenBuffer)
        makeScreenshot(testOutputScreenshot, gb.mmu.io.lcd.screenBuffer)
        testOutputHash.writeText("$hash")

        Log.i("Hash: $hash")
        Assert.assertNotNull(inputHashURI)
        Assert.assertEquals(inputHash, hash)
    }
}