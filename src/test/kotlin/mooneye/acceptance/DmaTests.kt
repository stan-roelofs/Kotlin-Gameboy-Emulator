package mooneye.acceptance

import mooneye.MooneyeTest
import org.junit.Test

class DmaTests : MooneyeTest() {
    override val path = "acceptance/oam_dma"

    @Test
    fun basic() {
        runMooneyeTest("basic.gb", -1059871625)
    }

    @Test
    fun reg_read() {
        runMooneyeTest("reg_read.gb", -1059871625)
    }

    @Test
    fun sources() {
        runMooneyeTest("sources-dmgABCmgbS.gb", -1059871625)
    }
}