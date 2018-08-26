package mooneye.acceptance

import GameBoy
import mooneye.MooneyeTest
import org.junit.Test
import java.io.File

class InterruptsTests : MooneyeTest() {
    override val path = "$pathToTests/acceptance/interrupts/"

    @Test
    fun ie_push() {
        val gb = GameBoy(File("${path}ie_push.gb"))
        runMooneyeTest(gb, 0)
    }
}