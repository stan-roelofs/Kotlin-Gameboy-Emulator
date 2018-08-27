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
}