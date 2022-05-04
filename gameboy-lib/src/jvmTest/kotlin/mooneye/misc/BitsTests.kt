package mooneye.misc

import mooneye.MooneyeTest
import kotlin.test.Ignore
import kotlin.test.Test

class BitsTests : MooneyeTest() {
    override val path = "misc/bits"

    @Test @Ignore("TODO: Doesn't pass yet")
    fun unused_hwio_c() {
        runMooneyeTest("unused_hwio-C.gb", Type.REGISTERS, true)
    }
}