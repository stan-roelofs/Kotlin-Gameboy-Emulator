package mooneye.acceptance

import GameBoy
import mooneye.MooneyeTest
import org.junit.Test
import java.io.File

class TimerTests : MooneyeTest() {
    override val path = "$pathToTests/acceptance/timer/"

    @Test
    fun div_write() {
        val gb = GameBoy(File("${path}div_write.gb"))
        runMooneyeTest(gb, 2123546873)
    }

    @Test
    fun rapid_toggle() {
        val gb = GameBoy(File("${path}rapid_toggle.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun tim00() {
        val gb = GameBoy(File("${path}tim00.gb"))
        runMooneyeTest(gb, -1376469881)
    }

    @Test
    fun tim00_div_trigger() {
        val gb = GameBoy(File("${path}tim00_div_trigger.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun tim01() {
        val gb = GameBoy(File("${path}tim01.gb"))
        runMooneyeTest(gb, 44913228)
    }

    @Test
    fun tim01_div_trigger() {
        val gb = GameBoy(File("${path}tim01_div_trigger.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun tim10() {
        val gb = GameBoy(File("${path}tim10.gb"))
        runMooneyeTest(gb, -1376469881)
    }

    @Test
    fun tim10_div_trigger() {
        val gb = GameBoy(File("${path}tim10_div_trigger.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun tim11() {
        val gb = GameBoy(File("${path}tim11.gb"))
        runMooneyeTest(gb, -1376469881)
    }

    @Test
    fun tim11_div_trigger() {
        val gb = GameBoy(File("${path}tim11_div_trigger.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun tima_reload() {
        val gb = GameBoy(File("${path}tima_reload.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun tima_write_reloading() {
        val gb = GameBoy(File("${path}tima_write_reloading.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun tma_write_reloading() {
        val gb = GameBoy(File("${path}tma_write_reloading.gb"))
        runMooneyeTest(gb, 0)
    }
}