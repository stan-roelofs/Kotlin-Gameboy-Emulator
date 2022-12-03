package mooneye.acceptance

import mooneye.MooneyeTest
import org.junit.Test

class BitsTests : MooneyeTest() {
    override val path = "acceptance/bits"

    @Test
    fun mem_oam() {
        runMooneyeTest("mem_oam.gb")
    }

    @Test
    fun reg_f() {
        runMooneyeTest("reg_f.gb")
    }

    @Test
    fun unused_hwio_GS() {
        runMooneyeTest("unused_hwio-GS.gb")
    }
}