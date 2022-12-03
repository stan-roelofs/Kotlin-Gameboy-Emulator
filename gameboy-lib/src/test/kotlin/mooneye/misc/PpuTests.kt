package mooneye.misc

import mooneye.MooneyeTest
import org.junit.Ignore
import org.junit.Test

class PpuTests : MooneyeTest() {
    override val path = "misc/ppu"

    @Test @Ignore("TODO: Doesn't pass yet")
    fun vblank_stat_intr_c() {
        runMooneyeTest("vblank_stat_intr-C.gb", Type.REGISTERS, true)
    }
}