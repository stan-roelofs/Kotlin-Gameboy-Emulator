import org.junit.Assert.assertEquals
import org.junit.Test

internal class IntExtensionsTest {

    @Test
    fun getFirstByte() {
        val a = 0b1011111110101100
        assertEquals(172, a.getFirstByte())
    }

    @Test
    fun getSecondByte() {
        val a = 0b1011111110101100
        assertEquals(191, a.getSecondByte())
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
        a = setBit(a, 0, true)
        assertEquals(1, a)
        a = setBit(a, 2, true)
        assertEquals(5, a)
        a = clearBit(a, 2)
        assertEquals(1, a)
        a = clearBit(a, 0)
        assertEquals(0, a)
    }
}