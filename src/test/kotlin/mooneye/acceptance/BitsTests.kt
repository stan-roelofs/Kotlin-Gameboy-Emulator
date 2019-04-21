package mooneye.acceptance

import GameBoy
import mooneye.MooneyeTest
import org.junit.Test
import java.io.File

class BitsTests : MooneyeTest() {
    override val path = "$pathToTests/acceptance/bits/"

    @Test
    fun mem_oam() {
        val gb = GameBoy(File("${path}mem_oam.gb"))
        runMooneyeTest(gb, -1059871625)
    }

    @Test
    fun reg_f() {
        val gb = GameBoy(File("${path}reg_f.gb"))
        runMooneyeTest(gb, -1322830258)
    }

    @Test
    fun unused_hwio_GS() {
        val gb = GameBoy(File("${path}unused_hwio-GS.gb"))
        runMooneyeTest(gb, -1059871625)
    }
}