package mooneye.acceptance

import mooneye.MooneyeTest
import kotlin.test.Test

class InterruptsTests : MooneyeTest() {
    override val path = "acceptance/interrupts"

    @Test
    fun ie_push() {
        runMooneyeTest("ie_push.gb")
    }
}