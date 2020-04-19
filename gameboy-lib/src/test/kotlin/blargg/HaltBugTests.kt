package blargg

import org.junit.Test

internal class HaltBugTests : BlarggTest() {

    override val path = "/"
    override val method = ValidateMethod.SCREENHASH

    @Test
    fun haltBug() {
        runBlarggTest("halt_bug.gb", 1690364409)
    }
}