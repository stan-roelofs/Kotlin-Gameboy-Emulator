package blargg

import GameBoy
import org.junit.Test
import java.io.File

internal class CpuInstrsTests : BlarggTest() {

    override val path = "${pathToTests}cpu_instrs/individual/"

    @Test
    fun test1() {
        val gb = GameBoy(File("${path}01-special.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test2() {
        val gb = GameBoy(File("${path}02-interrupts.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test3() {
        val gb = GameBoy(File("${path}03-op sp,hl.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test4() {
        val gb = GameBoy(File("${path}04-op r,imm.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test5() {
        val gb = GameBoy(File("${path}05-op rp.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test6() {
        val gb = GameBoy(File("${path}06-ld r,r.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test7() {
        val gb = GameBoy(File("${path}07-jr,jp,call,ret,rst.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test8() {
        val gb = GameBoy(File("${path}08-misc instrs.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test9() {
        val gb = GameBoy(File("${path}09-op r,r.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test10() {
        val gb = GameBoy(File("${path}10-bit ops.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test11() {
        val gb = GameBoy(File("${path}11-op a,(hl).gb"))
        runBlarggTest(gb)
    }
}