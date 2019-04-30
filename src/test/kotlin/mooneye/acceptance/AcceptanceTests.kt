package mooneye.acceptance

import GameBoy
import mooneye.MooneyeTest
import org.junit.Test
import java.io.File

class AcceptanceTests : MooneyeTest() {

    override val path = pathToTests + "acceptance/"

    @Test
    fun add_sp_e_timing() {
        val gb = GameBoy(File("${path}add_sp_e_timing.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun boot_div_dmgABC() {
        val gb = GameBoy(File("${path}boot_div-dmgABCmgb.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun boot_hwio_dmgABC() {
        val gb = GameBoy(File("${path}boot_hwio-dmgABCmgb.gb"))
        runMooneyeTest(gb, -1059871625)
    }

    @Test
    fun boot_regs_dmgABC() {
        val gb = GameBoy(File("${path}boot_regs-dmgABC.gb"))
        runMooneyeTest(gb, 2030127056)
    }

    @Test
    fun call_cc_timing() {
        val gb = GameBoy(File("${path}call_cc_timing.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun call_cc_timing2() {
        val gb = GameBoy(File("${path}call_cc_timing2.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun call_timing() {
        val gb = GameBoy(File("${path}call_timing.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun call_timing2() {
        val gb = GameBoy(File("${path}call_timing2.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun di_timing_GS() {
        val gb = GameBoy(File("${path}di_timing-GS.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun div_timing() {
        val gb = GameBoy(File("${path}div_timing.gb"))
        runMooneyeTest(gb, 618563916)
    }

    @Test
    fun ei_sequence() {
        val gb = GameBoy(File("${path}ei_sequence.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun ei_timing() {
        val gb = GameBoy(File("${path}ei_timing.gb"))
        runMooneyeTest(gb, -2097771431)
    }

    @Test
    fun halt_ime0_ei() {
        val gb = GameBoy(File("${path}halt_ime0_ei.gb"))
        runMooneyeTest(gb, -1059871625)
    }

    @Test
    fun halt_ime0_nointr_timing() {
        val gb = GameBoy(File("${path}halt_ime0_nointr_timing.gb"))
        runMooneyeTest(gb, 427613825)
    }

    @Test
    fun halt_ime1_timing() {
        val gb = GameBoy(File("${path}halt_ime1_timing.gb"))
        runMooneyeTest(gb, -769890694)
    }

    @Test
    fun halt_ime1_timing2_GS() {
        val gb = GameBoy(File("${path}halt_ime1_timing2-GS.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun if_ie_registers() {
        val gb = GameBoy(File("${path}if_ie_registers.gb"))
        runMooneyeTest(gb, 1747996174)
    }

    @Test
    fun intr_timing() {
        val gb = GameBoy(File("${path}intr_timing.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun jp_cc_timing() {
        val gb = GameBoy(File("${path}jp_cc_timing.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun jp_timing() {
        val gb = GameBoy(File("${path}jp_timing.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun ld_hl_sp_e_timing() {
        val gb = GameBoy(File("${path}ld_hl_sp_e_timing.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun oam_dma_restart() {
        val gb = GameBoy(File("${path}oam_dma_restart.gb"))
        runMooneyeTest(gb, 1906711267)
    }

    @Test
    fun oam_dma_start() {
        val gb = GameBoy(File("${path}oam_dma_start.gb"))
        runMooneyeTest(gb, -2065568537)
    }

    @Test
    fun oam_dma_timing() {
        val gb = GameBoy(File("${path}oam_dma_timing.gb"))
        runMooneyeTest(gb, 1906711267)
    }

    @Test
    fun pop_timing() {
        val gb = GameBoy(File("${path}pop_timing.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun push_timing() {
        val gb = GameBoy(File("${path}push_timing.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun rapid_di_ei() {
        val gb = GameBoy(File("${path}rapid_di_ei.gb"))
        runMooneyeTest(gb, 978629457)
    }

    @Test
    fun ret_cc_timing() {
        val gb = GameBoy(File("${path}ret_cc_timing.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun ret_timing() {
        val gb = GameBoy(File("${path}ret_timing.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun reti_intr_timing() {
        val gb = GameBoy(File("${path}reti_intr_timing.gb"))
        runMooneyeTest(gb, 1502539589)
    }

    @Test
    fun reti_timing() {
        val gb = GameBoy(File("${path}reti_timing.gb"))
        runMooneyeTest(gb, 0)
    }

    @Test
    fun rst_timing() {
        val gb = GameBoy(File("${path}rst_timing.gb"))
        runMooneyeTest(gb, 0)
    }
}