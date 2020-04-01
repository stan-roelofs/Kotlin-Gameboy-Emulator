package blargg

import org.junit.Test

internal class MemTimingTests : BlarggTest() {

    override val path = "mem_timing/individual"
    override val method = ValidateMethod.SERIAL

    @Test
    fun test1() {
        runBlarggTest("01-read_timing.gb")
    }

    @Test
    fun test2() {
        runBlarggTest("02-write_timing.gb")
    }

    @Test
    fun test3() {
        runBlarggTest("03-modify_timing.gb")
    }
}