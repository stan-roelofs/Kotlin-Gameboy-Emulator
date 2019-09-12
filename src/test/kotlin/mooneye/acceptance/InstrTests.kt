package mooneye.acceptance

import mooneye.MooneyeTest
import org.junit.Test

class InstrTests : MooneyeTest() {
    override val path = "acceptance/instr"

    @Test
    fun daa() {
        runMooneyeTest("daa.gb", -1059871625)
    }
}