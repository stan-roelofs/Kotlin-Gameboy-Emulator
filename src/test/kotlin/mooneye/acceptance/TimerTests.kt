package mooneye.acceptance

import mooneye.MooneyeTest
import org.junit.Test

class TimerTests : MooneyeTest() {
    override val path = "acceptance/timer"

    @Test
    fun div_write() {
        runMooneyeTest("div_write.gb", -1059871625)
    }

    @Test
    fun rapid_toggle() {
        runMooneyeTest("rapid_toggle.gb", 488748970)
    }

    @Test
    fun tim00() {
        runMooneyeTest("tim00.gb", 1674011845)
    }

    @Test
    fun tim00_div_trigger() {
        runMooneyeTest("tim00_div_trigger.gb", 1674011845)
    }

    @Test
    fun tim01() {
        runMooneyeTest("tim01.gb", -352991524)
    }

    @Test
    fun tim01_div_trigger() {
        runMooneyeTest("tim01_div_trigger.gb", -245527860)
    }

    @Test
    fun tim10() {
        runMooneyeTest("tim10.gb", 1674011845)
    }

    @Test
    fun tim10_div_trigger() {
        runMooneyeTest("tim10_div_trigger.gb", -774197449)
    }

    @Test
    fun tim11() {
        runMooneyeTest("tim11.gb", 1674011845)
    }

    @Test
    fun tim11_div_trigger() {
        runMooneyeTest("tim11_div_trigger.gb", 1674011845)
    }

    /*
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
    }*/
}