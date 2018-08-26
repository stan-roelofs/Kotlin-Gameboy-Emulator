package blargg

import GameBoy
import org.junit.Test
import java.io.File

class InstrTimingTests : BlarggTest() {

    override val path = "$pathToTests/instr_timing/"
    @Test
    fun testInstrTiming() {
        val gb = GameBoy(File("${path}instr_timing.gb"))
        runBlarggTest(gb)
    }
}