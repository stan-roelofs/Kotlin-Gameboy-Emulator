package mooneye.acceptance

import mooneye.MooneyeTest
import kotlin.test.Ignore
import kotlin.test.Test

class AcceptanceTests : MooneyeTest() {

    override val path = "acceptance"

    @Test
    fun add_sp_e_timing() {
        runMooneyeTest("add_sp_e_timing.gb")
    }

    @Test
    fun boot_div_dmgABC() {
        runMooneyeTest("boot_div-dmgABCmgb.gb")
    }

    @Test @Ignore("TODO: Doesn't pass yet")
    fun boot_hwio_dmgABC() {
        runMooneyeTest("boot_hwio-dmgABCmgb.gb")
    }

    @Test
    fun boot_regs_dmgABC() {
        runMooneyeTest("boot_regs-dmgABC.gb")
    }

    @Test
    fun call_cc_timing() {
        runMooneyeTest("call_cc_timing.gb")
    }

    @Test
    fun call_cc_timing2() {
        runMooneyeTest("call_cc_timing2.gb")
    }

    @Test
    fun call_timing() {
        runMooneyeTest("call_timing.gb")
    }

    @Test
    fun call_timing2() {
        runMooneyeTest("call_timing2.gb")
    }

    @Test
    fun di_timing_GS() {
        runMooneyeTest("boot_regs-dmgABC.gb")
    }

    @Test
    fun div_timing() {
        runMooneyeTest("div_timing.gb")
    }

    @Test @Ignore("TODO: Doesn't pass yet")
    fun ei_sequence() {
        runMooneyeTest("ei_sequence.gb")
    }

    @Test
    fun ei_timing() {
        runMooneyeTest("ei_timing.gb")
    }

    @Test
    fun halt_ime0_ei() {
        runMooneyeTest("halt_ime0_ei.gb")
    }

    @Test @Ignore("TODO: Doesn't pass yet")
    fun halt_ime0_nointr_timing() {
        runMooneyeTest("halt_ime0_nointr_timing.gb")
    }

    @Test
    fun halt_ime1_timing() {
        runMooneyeTest("halt_ime1_timing.gb")
    }


    @Test @Ignore("TODO: Doesn't pass yet")
    fun halt_ime1_timing2_GS() {
        runMooneyeTest("halt_ime1_timing2-GS.gb")
    }

    @Test
    fun if_ie_registers() {
        runMooneyeTest("if_ie_registers.gb")
    }


    @Test
    fun intr_timing() {
        runMooneyeTest("intr_timing.gb")
    }

    @Test
    fun jp_cc_timing() {
        runMooneyeTest("jp_cc_timing.gb")
    }

    @Test
    fun jp_timing() {
        runMooneyeTest("jp_timing.gb")
    }

    @Test
    fun ld_hl_sp_e_timing() {
        runMooneyeTest("ld_hl_sp_e_timing.gb")
    }

    @Test
    fun oam_dma_restart() {
        runMooneyeTest("oam_dma_restart.gb")
    }

    @Test
    fun oam_dma_start() {
        runMooneyeTest("oam_dma_start.gb")
    }

    @Test
    fun oam_dma_timing() {
        runMooneyeTest("oam_dma_timing.gb")
    }

    @Test
    fun pop_timing() {
        runMooneyeTest("pop_timing.gb")
    }

    @Test
    fun push_timing() {
        runMooneyeTest("push_timing.gb")
    }

    @Test
    fun rapid_di_ei() {
        runMooneyeTest("rapid_di_ei.gb")
    }

    @Test
    fun ret_cc_timing() {
        runMooneyeTest("ret_cc_timing.gb")
    }

    @Test
    fun ret_timing() {
        runMooneyeTest("ret_timing.gb")
    }

    @Test
    fun reti_intr_timing() {
        runMooneyeTest("reti_intr_timing.gb")
    }

    @Test
    fun reti_timing() {
        runMooneyeTest("reti_timing.gb")
    }

    @Test
    fun rst_timing() {
        runMooneyeTest("rst_timing.gb")
    }
}