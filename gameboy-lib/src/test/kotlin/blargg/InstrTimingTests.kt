package blargg

import org.junit.Test

class InstrTimingTests : BlarggTestSerial() {

    override val path = "instr_timing"

    @Test
    fun testInstrTiming() {
        runBlarggTest("instr_timing.gb")
    }
}