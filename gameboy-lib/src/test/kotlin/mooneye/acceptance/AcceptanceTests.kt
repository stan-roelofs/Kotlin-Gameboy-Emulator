package mooneye.acceptance

import mooneye.MooneyeTest
import org.junit.Test

class AcceptanceTests : MooneyeTest() {

    override val path = "acceptance"

    @Test
    fun add_sp_e_timing() {
        runMooneyeTest("add_sp_e_timing.gb", 1253675478)
    }

    @Test
    fun boot_div_dmgABC() {
        runMooneyeTest("boot_div-dmgABCmgb.gb", 1228927159)
    }

    @Test
    fun boot_hwio_dmgABC() {
        runMooneyeTest("boot_hwio-dmgABCmgb.gb", 1864719267)
    }

    @Test
    fun boot_regs_dmgABC() {
        runMooneyeTest("boot_regs-dmgABC.gb", -1938941160)
    }

    @Test
    fun call_cc_timing() {
        runMooneyeTest("call_cc_timing.gb", 1864719267)
    }

    @Test
    fun call_cc_timing2() {
        runMooneyeTest("call_cc_timing2.gb", -1274294891)
    }

    @Test
    fun call_timing() {
        runMooneyeTest("call_timing.gb", 1864719267)
    }

    @Test
    fun call_timing2() {
        runMooneyeTest("call_timing2.gb", -889195877)
    }

    /*
    @Test
    fun di_timing_GS() {
        runMooneyeTest("boot_regs-dmgABC.gb", -1938941160)

    }*/

    @Test
    fun div_timing() {
        runMooneyeTest("div_timing.gb", 944462996)
    }

    /*
    @Test
    fun ei_sequence() {
        val gb = GameBoy(File("${path}ei_sequence.gb"))
        runMooneyeTest(gb, 0)
    }*/

    @Test
    fun ei_timing() {
        runMooneyeTest("ei_timing.gb", -1771872351)
    }

    @Test
    fun halt_ime0_ei() {
        runMooneyeTest("halt_ime0_ei.gb", 1864719267)
    }

    @Test
    fun halt_ime0_nointr_timing() {
        runMooneyeTest("halt_ime0_nointr_timing.gb", -1918004250)
    }

    @Test
    fun halt_ime1_timing() {
        runMooneyeTest("halt_ime1_timing.gb", -443991614)
    }

    /*
    @Test
    fun halt_ime1_timing2_GS() {
        val gb = GameBoy(File("${path}halt_ime1_timing2-GS.gb"))
        runMooneyeTest(gb, 0)
    }*/

    @Test
    fun if_ie_registers() {
        runMooneyeTest("if_ie_registers.gb", 2073895254)
    }


    @Test
    fun intr_timing() {
        runMooneyeTest("intr_timing.gb", 1412901067)
    }

    @Test
    fun jp_cc_timing() {
        runMooneyeTest("jp_cc_timing.gb", 1864719267)
    }

    @Test
    fun jp_timing() {
        runMooneyeTest("jp_timing.gb", 1864719267)
    }

    @Test
    fun ld_hl_sp_e_timing() {
        runMooneyeTest("ld_hl_sp_e_timing.gb", -1593236683)
    }

    @Test
    fun oam_dma_restart() {
        runMooneyeTest("oam_dma_restart.gb", 1809143252)
    }

    @Test
    fun oam_dma_start() {
        runMooneyeTest("oam_dma_start.gb", -41705204)
    }

    @Test
    fun oam_dma_timing() {
        runMooneyeTest("oam_dma_timing.gb", 1809143252)
    }

    @Test
    fun pop_timing() {
        runMooneyeTest("pop_timing.gb", 1938152555)
    }

    @Test
    fun push_timing() {
        runMooneyeTest("push_timing.gb", -1182071810)
    }

    @Test
    fun rapid_di_ei() {
        runMooneyeTest("rapid_di_ei.gb", 1304528537)
    }

    @Test
    fun ret_cc_timing() {
        runMooneyeTest("ret_cc_timing.gb", 1864719267)
    }

    @Test
    fun ret_timing() {
        runMooneyeTest("ret_timing.gb", 1864719267)
    }

    @Test
    fun reti_intr_timing() {
        runMooneyeTest("reti_intr_timing.gb", 1828438669)
    }

    @Test
    fun reti_timing() {
        runMooneyeTest("reti_timing.gb", 1864719267)
    }

    @Test
    fun rst_timing() {
        runMooneyeTest("rst_timing.gb", 892853141)
    }
}