package blargg

import org.junit.Test

internal class CpuInstrsTests : BlarggTest() {

    override val path = "cpu_instrs/individual"

    @Test
    fun test1() {
        runBlarggTest("01-special.gb")
    }

    @Test
    fun test2() {
        runBlarggTest("02-interrupts.gb")
    }

    @Test
    fun test3() {
        runBlarggTest("03-op sp,hl.gb")
    }

    @Test
    fun test4() {
        runBlarggTest("04-op r,imm.gb")
    }

    @Test
    fun test5() {
        runBlarggTest("05-op rp.gb")
    }

    @Test
    fun test6() {
        runBlarggTest("06-ld r,r.gb")
    }

    @Test
    fun test7() {
        runBlarggTest("07-jr,jp,call,ret,rst.gb")
    }

    @Test
    fun test8() {
        runBlarggTest("08-misc instrs.gb")
    }

    @Test
    fun test9() {
        runBlarggTest("09-op r,r.gb")
    }

    @Test
    fun test10() {
        runBlarggTest("10-bit ops.gb")
    }

    @Test
    fun test11() {
        runBlarggTest("11-op a,(hl).gb")
    }
}