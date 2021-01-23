package mooneye.manualonly

import mooneye.MooneyeTest
import org.junit.Test

class SpritePriority : MooneyeTest() {
    override val path = "manual-only"

    @Test
    fun sprite_priority() {
        runMooneyeTest("sprite_priority.gb")
    }
}