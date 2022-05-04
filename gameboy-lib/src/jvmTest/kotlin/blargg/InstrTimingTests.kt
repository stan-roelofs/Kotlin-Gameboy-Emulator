package blargg

import kotlin.test.Test

class InstrTimingTests : BlarggTestSerial() {

    override val path = "instr_timing"

    @Test
    fun testInstrTiming() {
        runBlarggTest("instr_timing.gb")
    }
}