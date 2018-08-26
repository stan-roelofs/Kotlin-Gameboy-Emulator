package blargg

import GameBoy
import org.junit.Assert

abstract class BlarggTest {

    protected val pathToTests = "E:/Downloads/gb-test-roms-master/"
    abstract val path: String

    fun runBlarggTest(gb: GameBoy) {
        var output = ""
        for (i in 0..10000000) {
            gb.step()
            if (gb.mmu.io.serial.testOutput) {
                val char = gb.mmu.readByte(0xFF01).toChar()
                output += char
                gb.mmu.io.serial.testOutput = false
            }
        }
        println(output)
        val result = output.toLowerCase().contains("passed")
        Assert.assertEquals(true, result)
    }
}