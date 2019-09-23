package mooneye.emulatoronly

import mooneye.MooneyeTest
import org.junit.Test

class Mbc1Tests : MooneyeTest() {
    override val path = "emulator-only/mbc1"

    /*
    @Test
    fun bits_ram_en() {
        runMooneyeTest("bits_ram_en.gb", -1059871625)
    }*/

    /*
    @Test
    fun multicart_rom_8mb() {
        runMooneyeTest("multicart_rom_8Mb.gb", -1059871625)
    }*/

    @Test
    fun bits_bank1() {
        runMooneyeTest("bits_bank1.gb", 1864719267)
    }

    @Test
    fun bits_bank2() {
        runMooneyeTest("bits_bank2.gb", 1864719267)
    }

    @Test
    fun bits_mode() {
        runMooneyeTest("bits_mode.gb", 1864719267)
    }

    @Test
    fun bits_ramg() {
        runMooneyeTest("bits_ramg.gb", 1864719267)
    }

    @Test
    fun ram64kb() {
        runMooneyeTest("ram_64kb.gb", 1864719267)
    }

    @Test
    fun ram256kb() {
        runMooneyeTest("ram_256kb.gb", 1864719267)
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
    fun rom4mb() {
        runMooneyeTest("rom_4Mb.gb", 1864719267)
    }

    @Test
    fun rom8mb() {
        runMooneyeTest("rom_8Mb.gb", 1864719267)
    }

    @Test
    fun rom16mb() {
        runMooneyeTest("rom_16Mb.gb", 1864719267)
    }

    @Test
    fun rom512kb() {
        runMooneyeTest("rom_512kb.gb", 1864719267)
    }
}