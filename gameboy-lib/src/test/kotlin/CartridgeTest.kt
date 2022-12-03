import nl.stanroelofs.gameboy.memory.cartridge.MBC1
import nl.stanroelofs.gameboy.utils.Buffer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class CartridgeTest {

    @Test
    fun ramSize0() {
        val cart = MBC1(2, 0, true)

        try {
            val buffer = Buffer<Byte>()
            cart.saveRam(buffer)
        } catch (e : Exception) {
            assertTrue(e is IllegalStateException)
        }
    }

    private fun testRam(size: Int, banks: Int) {
        val totalSize = size * banks

        val cart = MBC1(2, totalSize, true)
        cart.ramEnabled = true

        for (i in 0 until totalSize) {
            val currentBank = i / size
            val currentIndex = i % size
            cart.ram!![currentBank][currentIndex] = currentIndex
        }

        val buffer = Buffer<Byte>()
        cart.saveRam(buffer)

        assertEquals(totalSize, buffer.length())
        for (i in 0 until totalSize) {
            val currentBank = i / size
            val currentIndex = i % size
            assertEquals(buffer.get(i), cart.ram!![currentBank][currentIndex].toByte())
        }

        cart.reset()

        cart.loadRam(buffer)

        assertEquals(totalSize, buffer.length())
        for (i in 0 until totalSize) {
            val currentBank = i / size
            val currentIndex = i % size
            assertEquals(buffer.get(i), cart.ram!![currentBank][currentIndex].toByte())
        }
    }

    @Test
    fun ramSize800() {
        testRam(0x800, 1)
    }

    @Test
    fun ramSize2000() {
        testRam(0x2000, 1)
    }

    @Test
    fun ramSize8000() {
        testRam(0x2000, 4)
    }

    @Test
    fun ramSize20000() {
        testRam(0x2000, 16)
    }
}