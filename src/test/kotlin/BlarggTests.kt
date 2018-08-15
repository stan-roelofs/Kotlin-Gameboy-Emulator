import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

internal class BlarggTests {

    private val pathToTests = "E:/Downloads/gb-test-roms-master/"
    private val cpu_instrs = "cpu_instrs/individual/"
    private val instr_timing = "instr_timing/"

    fun runBlarggTest(gb: GameBoy) {
        var output = ""
        for (i in 0..10000000) {
            gb.step()
            if (gb.mmu.testOutput) {
                val char = gb.mmu.readByte(0xFF01).toChar()
                output += char
                gb.mmu.testOutput = false
            }
        }
        println(output)
        val result = output.toLowerCase().contains("passed")
        assertEquals(true, result)
    }

    @Test
    fun test1() {
        val gb = GameBoy(File("$pathToTests${cpu_instrs}01-special.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test2() {
        val gb = GameBoy(File("$pathToTests${cpu_instrs}02-interrupts.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test3() {
        val gb = GameBoy(File("$pathToTests${cpu_instrs}03-op sp,hl.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test4() {
        val gb = GameBoy(File("$pathToTests${cpu_instrs}04-op r,imm.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test5() {
        val gb = GameBoy(File("$pathToTests${cpu_instrs}05-op rp.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test6() {
        val gb = GameBoy(File("$pathToTests${cpu_instrs}06-ld r,r.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test7() {
        val gb = GameBoy(File("$pathToTests${cpu_instrs}07-jr,jp,call,ret,rst.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test8() {
        val gb = GameBoy(File("$pathToTests${cpu_instrs}08-misc instrs.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test9() {
        val gb = GameBoy(File("$pathToTests${cpu_instrs}09-op r,r.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test10() {
        val gb = GameBoy(File("$pathToTests${cpu_instrs}10-bit ops.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test11() {
        val gb = GameBoy(File("$pathToTests${cpu_instrs}11-op a,(hl).gb"))
        runBlarggTest(gb)
    }

    @Test
    fun testInstrTiming() {
        val gb = GameBoy(File("$pathToTests${instr_timing}instr_timing.gb"))
        runBlarggTest(gb)
    }
}