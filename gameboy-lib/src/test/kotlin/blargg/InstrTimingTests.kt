package blargg

import org.junit.Test

class InstrTimingTests : BlarggTest() {

    override val path = "instr_timing"
    override val method = ValidateMethod.SERIAL

    @Test
    fun testInstrTiming() {
        runBlarggTest("instr_timing.gb")
    }
}