package mooneye.misc

import mooneye.MooneyeTest

class PpuTests : MooneyeTest() {
    override val path = "misc/ppu"

   // @Test
    fun vblank_stat_intr_c() {
        runMooneyeTest("vblank_stat_intr-C.gb", true)
    }
}