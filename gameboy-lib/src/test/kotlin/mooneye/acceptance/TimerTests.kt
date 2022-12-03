package mooneye.acceptance

import mooneye.MooneyeTest
import org.junit.Test

class TimerTests : MooneyeTest() {
    override val path = "acceptance/timer"

    @Test
    fun div_write() {
        runMooneyeTest("div_write.gb")
    }

    @Test
    fun rapid_toggle() {
        runMooneyeTest("rapid_toggle.gb")
    }

    @Test
    fun tim00() {
        runMooneyeTest("tim00.gb")
    }

    @Test
    fun tim00_div_trigger() {
        runMooneyeTest("tim00_div_trigger.gb")
    }

    @Test
    fun tim01() {
        runMooneyeTest("tim01.gb")
    }

    @Test
    fun tim01_div_trigger() {
        runMooneyeTest("tim01_div_trigger.gb")
    }

    @Test
    fun tim10() {
        runMooneyeTest("tim10.gb")
    }

    @Test
    fun tim10_div_trigger() {
        runMooneyeTest("tim10_div_trigger.gb")
    }

    @Test
    fun tim11() {
        runMooneyeTest("tim11.gb")
    }

    @Test
    fun tim11_div_trigger() {
        runMooneyeTest("tim11_div_trigger.gb")
    }

    @Test
    fun tima_reload() {
        runMooneyeTest("tima_reload.gb")
    }

    @Test
    fun tma_write_reloading() {
        runMooneyeTest("tma_write_reloading.gb")
    }

    @Test
    fun tima_write_reloading() {
        runMooneyeTest("tima_write_reloading.gb")
    }
}