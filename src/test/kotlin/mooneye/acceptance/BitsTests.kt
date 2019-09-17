package mooneye.acceptance

import mooneye.MooneyeTest
import org.junit.Test

class BitsTests : MooneyeTest() {
    override val path = "acceptance/bits"

    @Test
    fun mem_oam() {
        runMooneyeTest("mem_oam.gb", 1864719267)
    }

    @Test
    fun reg_f() {
        runMooneyeTest("reg_f.gb", -996931178)
    }

    @Test
    fun unused_hwio_GS() {
        runMooneyeTest("unused_hwio-GS.gb", 1864719267)
    }
}