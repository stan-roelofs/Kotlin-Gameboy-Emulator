package blargg

import GameBoy
import org.junit.Assert
import utils.Log
import java.io.File
import java.net.URI

abstract class BlarggTest {

    abstract val path: String

    fun runBlarggTest(fileName: String) {
        val url: URI = BlarggTest::class.java.classLoader.getResource("blargg/${path}/${fileName}")!!.toURI()
        val romFile = File(url)
        val gb = GameBoy(romFile)

        Log.i("")
        Log.i("Running Blargg Test: $fileName")
        Log.i("Using serial output to check pass/fail")

        var output = ""

        for (i in 0..100000000) {
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