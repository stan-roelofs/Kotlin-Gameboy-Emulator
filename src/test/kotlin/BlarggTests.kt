import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

internal class BlarggTests {

    private val pathToTests = "E:/Downloads/gb-test-roms-master/cpu_instrs/individual/"

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
        val gb = GameBoy(File("${pathToTests}01-special.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test2() {
        val gb = GameBoy(File("${pathToTests}02-interrupts.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test3() {
        val gb = GameBoy(File("${pathToTests}03-op sp,hl.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test4() {
        val gb = GameBoy(File("${pathToTests}04-op r,imm.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test5() {
        val gb = GameBoy(File("${pathToTests}05-op rp.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test6() {
        val gb = GameBoy(File("${pathToTests}06-ld r,r.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test7() {
        val gb = GameBoy(File("${pathToTests}07-jr,jp,call,ret,rst.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test8() {
        val gb = GameBoy(File("${pathToTests}08-misc instrs.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test9() {
        val gb = GameBoy(File("${pathToTests}09-op r,r.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test10() {
        val gb = GameBoy(File("${pathToTests}10-bit ops.gb"))
        runBlarggTest(gb)
    }

    @Test
    fun test11() {
        val gb = GameBoy(File("${pathToTests}11-op a,(hl).gb"))
        runBlarggTest(gb)
    }
}