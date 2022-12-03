package mooneye.acceptance

import mooneye.MooneyeTest
import org.junit.Test

class InterruptsTests : MooneyeTest() {
    override val path = "acceptance/interrupts"

    @Test
    fun ie_push() {
        runMooneyeTest("ie_push.gb")
    }
}