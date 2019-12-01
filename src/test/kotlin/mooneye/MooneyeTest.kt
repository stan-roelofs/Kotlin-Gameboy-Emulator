package mooneye

import GameBoy
import org.junit.Assert
import utils.Log
import java.io.File
import java.net.URI

abstract class MooneyeTest {

    abstract val path: String

    // Runs one of the Mooneye GB test roms
    // Test passes when a hash of the screenBuffer matches a provided hash
    // Provided hash is a hash of the screenBuffer after a test passed
    fun runMooneyeTest(fileName: String, hashCode: Int) {
        val url: URI = MooneyeTest::class.java.classLoader.getResource("mooneye/${path}/${fileName}")!!.toURI()
        val romFile = File(url)
        val gb = GameBoy(romFile)

        Log.i("")
        Log.i("Running Mooneye Test: $fileName")
        Log.i("Provided hash $hashCode")

        // TODO: theres probably a better way to do this...
        for (i in 0..50000000) {
            gb.step()
        }
        val hash = getScreenHash(gb.mmu.io.lcd.screenBuffer)
        Log.i("Hash: $hash")
        Assert.assertEquals(hashCode, hash)
    }

    private fun getScreenHash(screen: Array<IntArray>): Int {
        var s = ""
        for (i in screen) {
            for (j in i) {
                s += j.toString()
            }
        }
        return s.hashCode()
    }
}