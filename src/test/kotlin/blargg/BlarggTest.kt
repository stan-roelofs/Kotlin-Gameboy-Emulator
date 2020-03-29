package blargg

import GameBoy
import org.junit.Assert
import utils.Log
import java.io.File
import java.net.URI

abstract class BlarggTest {

    val MAX_ITERATIONS = 100000000

    enum class ValidateMethod {
        SERIAL, MEMORY
    }

    abstract val path: String
    abstract val method: ValidateMethod

    fun runBlarggTest(fileName: String) {
        val url: URI = BlarggTest::class.java.classLoader.getResource("blargg/$path/$fileName")!!.toURI()
        val romFile = File(url)
        val gb = GameBoy(romFile)

        Log.i("")
        Log.i("Running Blargg Test: $fileName")

        when (method) {
            // This method prints a status to serial output
            ValidateMethod.SERIAL -> {
                Log.i("Using serial output to validate")

                var output = ""
                for (i in 0..MAX_ITERATIONS) {
                    gb.step()
                    if (gb.mmu.io.serial.testOutput) {
                        val char = gb.mmu.readByte(0xFF01).toChar()
                        output += char
                        gb.mmu.io.serial.testOutput = false
                    }
                }
                println("Finished test with output: $output")

                val result = output.toLowerCase().contains("passed")
                Assert.assertEquals(true, result)
                return
            }

            /*
             * 0xA000 holds the test status code. While the test is running it is set to 0x80.
             * 0xA001-0xA003 indicate that the data is actually from a test and not random and should equal 0xDE, 0xB0, 0x61
             * Text output is appended to 0xA004, terminated by 0
             */
            ValidateMethod.MEMORY -> {
                Log.i("Using memory at A000 to validate")

                var i = 0
                var status = false
                while ((!status || gb.mmu.readByte(0xA000) == 0x80) && i < MAX_ITERATIONS) {
                    gb.step()
                    i++

                    if (!status && gb.mmu.readByte(0xA000) == 0x80) {
                        status = true
                    }
                }

                if (i == MAX_ITERATIONS) {
                    Log.w("Max iterations reached")
                }

                Log.i("Test finished after $i iterations")

                Assert.assertEquals(0xDE, gb.mmu.readByte(0xA001))
                Assert.assertEquals(0xB0, gb.mmu.readByte(0xA002))
                Assert.assertEquals(0x61, gb.mmu.readByte(0xA003))

                val statusCode = gb.mmu.readByte(0xA000)

                var output = ""
                var currentAddress = 0
                while (gb.mmu.readByte(0xA004 + currentAddress) != 0) {
                    val char = gb.mmu.readByte(0xA004 + currentAddress).toChar()
                    output += char
                    currentAddress++
                }

                println("Finished test with status code: $statusCode and output: $output")
            }
        }
    }
}