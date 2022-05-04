import nl.stanroelofs.gameboy.utils.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class IntExtensionsTest {

    @Test
    fun getFirstByte() {
        val a = 0b1011111110101100
        assertEquals(172, a.firstByte)
    }

    @Test
    fun getSecondByte() {
        val a = 0b1011111110101100
        assertEquals(191, a.secondByte)
    }

    @Test
    fun getBit() {
        var a = 0b1
        assertEquals(true, a.getBit(0))
        a = 0b01
        assertEquals(true, a.getBit(0))
        assertEquals(false, a.getBit(1))
        a = 0b0
        assertEquals(false, a.getBit(0))
    }

    @Test
    fun setBit() {
        var a = 0b0
        assertEquals(0, a)
        a = a.setBit(0, true)
        assertEquals(1, a)
        a = a.setBit(2, true)
        assertEquals(5, a)
        a = a.clearBit(2)
        assertEquals(1, a)
        a = a.clearBit(0)
        assertEquals(0, a)
    }
}