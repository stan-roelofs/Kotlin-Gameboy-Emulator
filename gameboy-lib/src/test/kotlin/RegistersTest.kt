import nl.stanroelofs.gameboy.cpu.RegistersDMG
import org.junit.Assert.assertEquals
import org.junit.Test

internal class RegistersTest {

    @Test
    fun testSet8BitRegister() {
        val r = RegistersDMG()

        // Normal test
        r.A = 5
        assertEquals(5, r.A)

        // Test 'incorrect' input is handled correctly
        r.A = 0xFFF
        assertEquals(0xFF, r.A)
    }

    @Test
    fun testSet16BitRegister() {
        val r = RegistersDMG()

        // Normal test
        r.setAF(0x1032)
        assertEquals(0x1030, r.getAF()) // Expecting 1030 since last 4 BitsTests of F are always 0
        r.setBC(0x1032)
        assertEquals(0x1032, r.getBC())
        r.setDE(0x1032)
        assertEquals(0x1032, r.getDE())
        r.setHL(0x1032)
        assertEquals(0x1032, r.getHL())
        r.SP = 0x1032
        assertEquals(0x1032, r.SP)
        r.PC = 0x1032
        assertEquals(0x1032, r.PC)

        // Test 'incorrect' input is handled correctly
        r.setAF(0xFFFFF)
        assertEquals(0xFFF0, r.getAF())
        r.setBC(0xFFFFF)
        assertEquals(0xFFFF, r.getBC())
        r.setDE(0xFFFFF)
        assertEquals(0xFFFF, r.getDE())
        r.setHL(0xFFFFF)
        assertEquals(0xFFFF, r.getHL())
        r.SP = 0xFFFFF
        assertEquals(0xFFFF, r.SP)
        r.PC = 0xFFFFF
        assertEquals(0xFFFF, r.PC)
    }

    @Test
    fun testSetFlags() {
        val r = RegistersDMG()
        r.setCFlag(true)
        r.setHFlag(true)
        r.setNFlag(true)
        r.setZFlag(true)

        assertEquals(true, r.getCFlag())
        assertEquals(true, r.getHFlag())
        assertEquals(true, r.getNFlag())
        assertEquals(true, r.getZFlag())

        r.setCFlag(false)
        r.setHFlag(false)
        r.setNFlag(false)
        r.setZFlag(false)

        assertEquals(false, r.getCFlag())
        assertEquals(false, r.getHFlag())
        assertEquals(false, r.getNFlag())
        assertEquals(false, r.getZFlag())
    }

    @Test
    fun testIncSP() {
        val r = RegistersDMG()
        r.SP = 0

        // Normal test
        r.incSP()
        assertEquals(1, r.SP)

        // Test overflow
        r.SP = 0xFFFF
        r.incSP()
        assertEquals(0, r.SP)
    }

    @Test
    fun testDecSP() {
        val r = RegistersDMG()

        // Normal test
        r.SP = 16
        r.decSP()
        assertEquals(15, r.SP)

        // Test overflow
        r.SP = 0
        r.decSP()
        assertEquals(0xFFFF, r.SP)
    }

    @Test
    fun testIncPC() {
        val r = RegistersDMG()

        r.PC = 0

        // Normal test
        r.incPC()
        assertEquals(1, r.PC)

        // Test overflow
        r.PC = 0xFFFF
        r.incPC()
        assertEquals(0, r.PC)
    }
}