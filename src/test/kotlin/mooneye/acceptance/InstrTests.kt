package mooneye.acceptance

import GameBoy
import mooneye.MooneyeTest
import org.junit.Test
import java.io.File

class InstrTests : MooneyeTest() {
    override val path = "$pathToTests/acceptance/instr/"

    @Test
    fun daa() {
        val gb = GameBoy(File("${path}daa.gb"))
        runMooneyeTest(gb, -1059871625)
    }
}