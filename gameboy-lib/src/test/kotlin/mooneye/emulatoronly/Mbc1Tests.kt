package mooneye.emulatoronly

import mooneye.MooneyeTest
import org.junit.Test

class Mbc1Tests : MooneyeTest() {
    override val path = "emulator-only/mbc1"

    /*
    @Test
    fun multicart_rom_8mb() {
        runMooneyeTest("multicart_rom_8Mb.gb", -1059871625)
    }*/

    @Test
    fun bits_bank1() {
        runMooneyeTest("bits_bank1.gb")
    }

    @Test
    fun bits_bank2() {
        runMooneyeTest("bits_bank2.gb")
    }

    @Test
    fun bits_mode() {
        runMooneyeTest("bits_mode.gb")
    }

    @Test
    fun bits_ramg() {
        runMooneyeTest("bits_ramg.gb")
    }

    @Test
    fun ram64kb() {
        runMooneyeTest("ram_64kb.gb")
    }

    @Test
    fun ram256kb() {
        runMooneyeTest("ram_256kb.gb")
    }

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
    fun rom512kb() {
        runMooneyeTest("rom_512kb.gb")
    }
}