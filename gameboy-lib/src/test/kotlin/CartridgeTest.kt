import nl.stanroelofs.gameboy.memory.cartridge.MBC1
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.nio.file.Files

internal class CartridgeTest {

    @Test
    fun ramSize0() {
        val cart = MBC1(2, 0, true)

        val file = File("test")

        try {
            cart.saveRam(file)
        } catch (e : Exception) {
            assert(e is IllegalStateException)
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

        val file = File("test")
        cart.saveRam(file)

        cart.reset()
        for (i in cart.ram!!.indices) {
            for (j in i until cart.ram!![i].size) {
                assertEquals(cart.ram!![i][j], 0)
            }
        }

        cart.loadRam(file)

        val bytes = Files.readAllBytes(file.toPath())

        for (i in 0 until totalSize) {
            val currentBank = i / size
            val currentIndex = i % size
            assertEquals(bytes[i], cart.ram!![currentBank][currentIndex].toByte())
        }

        file.delete()
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