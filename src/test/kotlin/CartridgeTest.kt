import memory.cartridge.MBC1
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.nio.file.Files

internal class CartridgeTest {

    @Test
    fun ramSize0() {
        val cart = MBC1(2, 0)

        val file = File("test")

        try {
            cart.saveRam(file)
        } catch (e : Exception) {
            assert(e is IllegalArgumentException)
        }
    }

    @Test
    fun ramSize800() {
        val cart = MBC1(2, 0x800)
        cart.ramEnabled = true

        for (i in 0 until 0x800) {
            cart.ram!![0][i] = i
        }

        val file = File("test")
        cart.saveRam(file)

        cart.reset()
        for (i in 0 until cart.ram!!.size) {
            for (j in i until cart.ram!![i].size) {
                assertEquals(cart.ram!![i][j], 0)
            }
        }

        cart.loadRam(file)

        val bytes = Files.readAllBytes(file.toPath())
        val match = true

        for (i in 0 until 0x800) {
            assertEquals(bytes[i], cart.ram!![0][i].toByte())
        }
    }
}