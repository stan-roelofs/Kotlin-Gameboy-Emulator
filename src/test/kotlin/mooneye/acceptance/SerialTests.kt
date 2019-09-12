package mooneye.acceptance

import mooneye.MooneyeTest

class SerialTests : MooneyeTest() {
    override val path = "acceptance/serial"

    /*
    @Test
    fun boot_sclk_align_dmgABCmgb() {
        val gb = GameBoy(File("${path}boot_sclk_align-dmgABCmgb.gb"))
        runMooneyeTest(gb, 0)
    }*/
}