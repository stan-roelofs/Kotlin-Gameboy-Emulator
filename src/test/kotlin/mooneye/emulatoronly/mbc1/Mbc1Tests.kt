package mooneye.emulatoronly.mbc1

import mooneye.MooneyeTest
import org.junit.Test

class Mbc1Tests : MooneyeTest() {
    override val path = "emulator-only/mbc1"

    @Test
    fun bits_ram_en() {
        runMooneyeTest("bits_ram_en.gb", -1059871625)
    }

    @Test
    fun multicart_rom_8mb() {
        runMooneyeTest("multicart_rom_8Mb.gb", -1059871625)
    }

    @Test
    fun ram64kb() {
        runMooneyeTest("ram_64Kb.gb", -1059871625)
    }

    @Test
    fun ram256kb() {
        runMooneyeTest("ram_256Kb.gb", -1059871625)
    }

    @Test
    fun rom1mb() {
        runMooneyeTest("rom_1Mb.gb", -1059871625)
    }

    @Test
    fun rom2mb() {
        runMooneyeTest("rom_2Mb.gb", -1059871625)
    }

    @Test
    fun rom4mb() {
        runMooneyeTest("rom_4Mb.gb", -1059871625)
    }

    @Test
    fun rom8mb() {
        runMooneyeTest("rom_8Mb.gb", -1059871625)
    }

    @Test
    fun rom16mb() {
        runMooneyeTest("rom_16Mb.gb", -1059871625)
    }

    @Test
    fun rom512kb() {
        runMooneyeTest("rom_512Kb.gb", -1059871625)
    }
}