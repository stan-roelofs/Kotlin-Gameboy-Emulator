package mooneye.acceptance

import mooneye.MooneyeTest
import kotlin.test.Test

class InstrTests : MooneyeTest() {
    override val path = "acceptance/instr"

    @Test
    fun daa() {
        runMooneyeTest("daa.gb")
    }
}