package mooneye.misc

import mooneye.MooneyeTest
import org.junit.Test

class BitsTests : MooneyeTest() {
    override val path = "misc/bits"

    @Test
    fun unused_hwio_c() {
        runMooneyeTest("unused_hwio-C.gb", Type.REGISTERS, true)
    }
}