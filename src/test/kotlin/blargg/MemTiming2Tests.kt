package blargg

import GameBoy
import org.junit.Assert
import org.junit.Test
import utils.Log
import java.io.File
import java.net.URI

internal class MemTiming2Tests : BlarggTest() {

    override val path = "mem_timing-2/rom_singles"
    override val method = ValidateMethod.SERIAL

    @Test
    fun test1() {
        runMemTest2("01-read_timing.gb")
    }

    @Test
    fun test2() {
        runMemTest2("02-write_timing.gb")
    }

    @Test
    fun test3() {
        runMemTest2("03-modify_timing.gb")
    }

    private fun runMemTest2(fileName: String) {
        val url: URI = BlarggTest::class.java.classLoader.getResource("blargg/${path}/${fileName}")!!.toURI()
        val romFile = File(url)
        val gb = GameBoy(romFile)

        Log.i("")
        Log.i("Running Blargg Test: $fileName")
        Log.i("Using serial output to check pass/fail")


        for (i in 0..100000000) {
            gb.step()
        }

        val status = gb.mmu.readByte(0xA000)
        println("Output status: $status")

        if (gb.mmu.readByte(0xA001) != 0xDE || gb.mmu.readByte(0xA002) != 0xB0 || gb.mmu.readByte(0xA003) != 0x61) {
            println("Output invalid, signature does not match")
        }

        Assert.assertEquals(0, status)
    }
}