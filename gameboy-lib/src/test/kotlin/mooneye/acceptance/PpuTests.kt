package mooneye.acceptance

import mooneye.MooneyeTest
import org.junit.Test

class PpuTests : MooneyeTest() {
    override val path = "acceptance/ppu"

    /*
    @Test
    fun hblank_ly_scx_timing_GS() {
        val gb = GameBoy(File("${path}hblank_ly_scx_timing-GS.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun intr_1_2_timing_GS() {
        val gb = GameBoy(File("${path}intr_1_2_timing-GS.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun intr_2_0_timing_GS() {
        val gb = GameBoy(File("${path}intr_2_0_timing-GS.gb"))
        runMooneyeTest(gb, 0)
    }*/

    @Test
    fun intr_2_mode0_timing() {
        runMooneyeTest("intr_2_mode0_timing.gb")
    }

    /*
    @Test
    fun intr_2_mode0_timing_sprites() {
        runMooneyeTest("intr_2_mode0_timing_sprites.gb", 0)
    }*/

    @Test
    fun intr_2_mode3_timing() {
        runMooneyeTest("intr_2_mode3_timing.gb")
    }

    /*
    @Test
    fun intr_2_oam_ok_timing() {
        val gb = GameBoy(File("${path}intr_2_oam_ok_timing.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun lcdon_timing_dmgABCmgbS() {
        val gb = GameBoy(File("${path}lcdon_timing-dmgABCmgbS.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun lcdon_write_timing_GS() {
        val gb = GameBoy(File("${path}lcdon_write_timing-GS.gb"))
        runMooneyeTest(gb, 0)
    }*/

    @Test
    fun stat_irq_blocking() {
        runMooneyeTest("stat_irq_blocking.gb")
    }

    /*
    @Test
    fun stat_lyc_onoff() {
        val gb = GameBoy(File("${path}stat_lyc_onoff.gb"))
        runMooneyeTest(gb, 0)
    }*/

    @Test
    fun vblank_stat_intr_GS() {
        runMooneyeTest("vblank_stat_intr-GS.gb")
    }
}