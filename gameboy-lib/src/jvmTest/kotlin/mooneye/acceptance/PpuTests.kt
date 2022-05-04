package mooneye.acceptance

import mooneye.MooneyeTest
import org.junit.Ignore
import org.junit.Test

class PpuTests : MooneyeTest() {
    override val path = "acceptance/ppu"

    @Test @Ignore("TODO: Doesn't pass yet")
    fun hblank_ly_scx_timing_GS() {
        runMooneyeTest("hblank_ly_scx_timing-GS.gb")
    }

    @Test @Ignore("TODO: Doesn't pass yet")
    fun intr_1_2_timing_GS() {
        runMooneyeTest("intr_1_2_timing-GS.gb")
    }

    @Test @Ignore("TODO: Doesn't pass yet")
    fun intr_2_0_timing() {
        runMooneyeTest("intr_2_0_timing-GS.gb")
    }

    @Test
    fun intr_2_mode0_timing() {
        runMooneyeTest("intr_2_mode0_timing.gb")
    }

    @Test @Ignore("TODO: Doesn't pass yet")
    fun intr_2_mode0_timing_sprites() {
        runMooneyeTest("intr_2_mode0_timing_sprites.gb")
    }

    @Test
    fun intr_2_mode3_timing() {
        runMooneyeTest("intr_2_mode3_timing.gb")
    }

    @Test @Ignore("TODO: Doesn't pass yet")
    fun intr_2_oam_ok_timing() {
        runMooneyeTest("intr_2_oam_ok_timing.gb")
    }

    @Test @Ignore("TODO: Doesn't pass yet")
    fun lcdon_timing_GS() {
        runMooneyeTest("lcdon_timing-GS.gb")
    }

    @Test @Ignore("TODO: Doesn't pass yet")
    fun lcdon_write_timing_GS() {
        runMooneyeTest("lcdon_write_timing-GS.gb")
    }

    @Test @Ignore("TODO: Doesn't pass yet")
    fun stat_irq_blocking() {
        runMooneyeTest("stat_irq_blocking.gb")
    }

    @Test @Ignore("TODO: Doesn't pass yet")
    fun stat_lyc_onoff() {
        runMooneyeTest("stat_lyc_onoff.gb")
    }

    @Test @Ignore("TODO: Doesn't pass yet")
    fun vblank_stat_intr_GS() {
        runMooneyeTest("vblank_stat_intr-GS.gb")
    }
}