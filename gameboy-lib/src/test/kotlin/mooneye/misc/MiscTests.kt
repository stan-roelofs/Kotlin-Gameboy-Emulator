package mooneye.misc

import mooneye.MooneyeTest
import org.junit.Test

class MiscTests : MooneyeTest() {
    override val path = "misc"

    @Test
    fun boot_div_cgb() {
        runMooneyeTest("boot_div-cgbABCDE.gb", Type.REGISTERS, true)
    }

    @Test
    fun boot_hwio_c() {
        runMooneyeTest("boot_hwio-C.gb", Type.REGISTERS, true)
    }

    @Test
    fun boot_regs_cgb() {
        runMooneyeTest("boot_regs-cgb.gb", Type.REGISTERS, true)
    }
}