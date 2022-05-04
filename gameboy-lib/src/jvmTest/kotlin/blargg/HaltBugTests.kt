package blargg

import kotlin.test.Test

internal class HaltBugTests : BlarggTestScreenhash() {

    override val path = "/"

    @Test
    fun haltBug() {
        runBlarggTest("halt_bug.gb")
    }
}