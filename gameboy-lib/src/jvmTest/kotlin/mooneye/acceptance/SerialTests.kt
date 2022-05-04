package mooneye.acceptance

import mooneye.MooneyeTest
import kotlin.test.Ignore
import kotlin.test.Test

class SerialTests : MooneyeTest() {
    override val path = "acceptance/serial"

    @Test @Ignore("TODO: Doesn't pass yet")
    fun boot_sclk_align_dmgABCmgb() {
        runMooneyeTest("boot_sclk_align-dmgABCmgb.gb")
    }
}