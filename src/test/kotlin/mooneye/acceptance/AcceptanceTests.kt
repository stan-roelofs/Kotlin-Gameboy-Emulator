package mooneye.acceptance

import GameBoy
import mooneye.MooneyeTest
import org.junit.Test
import java.io.File

class AcceptanceTests : MooneyeTest() {

    override val path = pathToTests + "acceptance/"

    @Test
    fun test1() {
        val gb = GameBoy(File(path))
        runMooneyeTest(gb, 0)
    }
}