package mooneye.acceptance

import GameBoy
import mooneye.MooneyeTest
import org.junit.Test
import java.io.File

class DmaTests : MooneyeTest() {
    override val path = "$pathToTests/acceptance/oam_dma/"

    @Test
    fun basic() {
        val gb = GameBoy(File("${path}basic.gb"))
        runMooneyeTest(gb, 2123546873)
    }

    @Test
    fun reg_read() {
        val gb = GameBoy(File("${path}reg_read.gb"))
        runMooneyeTest(gb, 2123546873)
    }

    @Test
    fun sources() {
        val gb = GameBoy(File("${path}sources-dmgABCmgbS.gb"))
        runMooneyeTest(gb, 2123546873)
    }
}