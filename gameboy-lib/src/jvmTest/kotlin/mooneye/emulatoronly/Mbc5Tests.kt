package mooneye.emulatoronly

import mooneye.MooneyeTest
import kotlin.test.Test

class Mbc5Tests : MooneyeTest() {
    override val path = "emulator-only/mbc5"

    @Test
    fun rom1mb() {
        runMooneyeTest("rom_1Mb.gb")
    }

    @Test
    fun rom2mb() {
        runMooneyeTest("rom_2Mb.gb")
    }

    @Test
    fun rom4mb() {
        runMooneyeTest("rom_4Mb.gb")
    }

    @Test
    fun rom8mb() {
        runMooneyeTest("rom_8Mb.gb")
    }

    @Test
    fun rom16mb() {
        runMooneyeTest("rom_16Mb.gb")
    }

    @Test
    fun rom32mb() {
        runMooneyeTest("rom_32Mb.gb")
    }

    @Test
    fun rom64mb() {
        runMooneyeTest("rom_64Mb.gb")
    }

    @Test
    fun rom512kb() {
        runMooneyeTest("rom_512kb.gb")
    }
}