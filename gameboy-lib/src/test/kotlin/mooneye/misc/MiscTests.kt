package mooneye.misc

import mooneye.MooneyeTest

class MiscTests : MooneyeTest() {
    override val path = "misc"

   // @Test
    fun boot_div_cgb() {
        runMooneyeTest("boot_div-cgbABCDE.gb", true)
    }

   // @Test
    fun boot_hwio_c() {
        runMooneyeTest("boot_hwio-C.gb", true)
    }

   // @Test
    fun boot_regs_cgb() {
        runMooneyeTest("boot_regs-cgb.gb", true)
    }
}