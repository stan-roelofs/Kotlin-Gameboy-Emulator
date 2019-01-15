package mooneye.emulatoronly.mbc1

import GameBoy
import mooneye.MooneyeTest
import org.junit.Test
import java.io.File

class Mbc1Tests : MooneyeTest() {
    override val path = "$pathToTests/emulator-only/mbc1/"

    @Test
    fun bits_ram_en() {
        val gb = GameBoy(File("${path}bits_ram_en.gb"))
        runMooneyeTest(gb, 2123546873)
    }

    @Test
    fun multicart_rom_8mb() {
        val gb = GameBoy(File("${path}multicart_rom_8Mb.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun ram64kb() {
        val gb = GameBoy(File("${path}ram_64Kb.gb"))
        runMooneyeTest(gb, 2123546873)
    }

    @Test
    fun ram256kb() {
        val gb = GameBoy(File("${path}ram_256Kb.gb"))
        runMooneyeTest(gb, 2123546873)
    }

    @Test
    fun rom1mb() {
        val gb = GameBoy(File("${path}rom_1Mb.gb"))
        runMooneyeTest(gb, 2123546873)
    }

    @Test
    fun rom2mb() {
        val gb = GameBoy(File("${path}rom_2Mb.gb"))
        runMooneyeTest(gb, 2123546873)
    }

    @Test
    fun rom4mb() {
        val gb = GameBoy(File("${path}rom_4Mb.gb"))
        runMooneyeTest(gb, 2123546873)
    }

    @Test
    fun rom8mb() {
        val gb = GameBoy(File("${path}rom_8Mb.gb"))
        runMooneyeTest(gb, 2123546873)
    }

    @Test
    fun rom16mb() {
        val gb = GameBoy(File("${path}rom_16Mb.gb"))
        runMooneyeTest(gb, 2123546873)
    }

    @Test
    fun rom512kb() {
        val gb = GameBoy(File("${path}rom_512Kb.gb"))
        runMooneyeTest(gb, 2123546873)
    }
}