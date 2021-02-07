package mooneye.acceptance

import mooneye.MooneyeTest
import org.junit.Test

class SerialTests : MooneyeTest() {
    override val path = "acceptance/serial"

    @Test
    fun boot_sclk_align_dmgABCmgb() {
        runMooneyeTest("boot_sclk_align-dmgABCmgb.gb")
    }
}