package mooneye.emulatoronly

import mooneye.MooneyeTest
import org.junit.Test

class Mbc2Tests : MooneyeTest() {
    override val path = "emulator-only/mbc2"

    @Test
    fun bits_ramg() {
        runMooneyeTest("bits_ramg.gb", 1864719267)
    }

    @Test
    fun bits_romb() {
        runMooneyeTest("bits_romb.gb", 1864719267)
    }

    @Test
    fun bits_unused() {
        runMooneyeTest("bits_unused.gb", 1864719267)
    }

    @Test
    fun ram() {
        runMooneyeTest("ram.gb", 1864719267)
    }

    @Test
    fun rom1mb() {
        runMooneyeTest("rom_1Mb.gb", 1864719267)
    }

    @Test
    fun rom2mb() {
        runMooneyeTest("rom_2Mb.gb", 1864719267)
    }

    @Test
    fun rom512kb() {
        runMooneyeTest("rom_512kb.gb", 1864719267)
    }
}