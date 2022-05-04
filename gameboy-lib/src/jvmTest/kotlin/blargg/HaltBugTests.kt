package blargg

import org.junit.Test

internal class HaltBugTests : BlarggTestScreenhash() {

    override val path = "/"

    @Test
    fun haltBug() {
        runBlarggTest("halt_bug.gb")
    }
}