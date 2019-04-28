package blargg

import GameBoy
import org.junit.Test
import java.io.File

internal class MemTimingTests : BlarggTest() {

    override val path = "${pathToTests}mem_timing/individual/"

    @Test
    fun test1() {
        val gb = GameBoy(File("${path}01-read_timing.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test2() {
        val gb = GameBoy(File("${path}02-write_timing.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test3() {
        val gb = GameBoy(File("${path}03-modify_timing.gb"))
        runBlarggTest(gb)
    }
}