import cpu.Cpu
import memory.Mmu
import memory.cartridge.Cartridge
import java.io.File

class GameBoy(cart: File?) : Runnable {
    companion object {
        val TICKS_PER_SEC = 4194304 / 4
    }

    val cpu = Cpu()
    val mmu = Mmu.instance
    lateinit var cartridge: Cartridge
    var running = false
    var paused = false

    init {
        if (cart != null) {
            loadCartridge(cart)
        }
    }

    fun reset() {
        mmu.reset()
        cpu.reset()
    }

    fun step() {
        cpu.step()
    }

    override fun run() {
        running = true
        while (running) {
            step()
        }
    }

    fun loadCartridge(cart: File) {
        cartridge = Cartridge(cart)
        mmu.cartridge = cartridge
        reset()
    }
}