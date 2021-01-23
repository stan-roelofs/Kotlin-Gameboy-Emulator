package mooneye.misc

import mooneye.MooneyeTest

class BitsTests : MooneyeTest() {
    override val path = "misc/bits"

    //@Test
    fun unused_hwio_c() {
        runMooneyeTest("unused_hwio-C.gb", true)
    }
}