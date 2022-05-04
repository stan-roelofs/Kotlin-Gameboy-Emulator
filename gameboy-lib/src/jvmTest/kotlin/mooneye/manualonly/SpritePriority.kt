package mooneye.manualonly

import mooneye.MooneyeTest
import kotlin.test.Test

class SpritePriority : MooneyeTest() {
    override val path = "manual-only"

    @Test
    fun sprite_priority() {
        runMooneyeTest("sprite_priority.gb", Type.SCREENHASH)
    }
}