package mooneye

import GameBoy
import org.junit.Assert
import utils.Log

abstract class MooneyeTest {

    protected val pathToTests = "E:/Downloads/mooneye-gb_hwtests/"
    abstract val path: String

    // Runs one of the Mooneye GB test roms
    // Test passes when a hash of the screen matches a provided hash
    // Provided hash is a hash of the screen after a test passed
    fun runMooneyeTest(gb: GameBoy, hashCode: Int) {
        Log.i("Running Mooneye Test")
        Log.i("Provided hash $hashCode")
        for (i in 0..10000000) {
            gb.step()
        }
        val hash = getScreenHash(gb.mmu.io.lcd.screen)
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