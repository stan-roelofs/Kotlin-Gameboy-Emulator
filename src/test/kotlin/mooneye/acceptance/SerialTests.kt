package mooneye.acceptance

import GameBoy
import mooneye.MooneyeTest
import org.junit.Test
import java.io.File

class SerialTests : MooneyeTest() {
    override val path = "$pathToTests/acceptance/serial/"

    @Test
    fun boot_sclk_align_dmgABCmgb() {
        val gb = GameBoy(File("${path}boot_sclk_align-dmgABCmgb.gb"))
        runMooneyeTest(gb, 0)
    }
}