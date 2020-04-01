import cpu.Cpu
import memory.Mmu
import memory.cartridge.Cartridge
import java.io.File

class GameBoy(cart: File?) : Runnable {
    companion object {
        const val TICKS_PER_SEC = 4194304 / 4
    }

    val cpu = Cpu()
    val mmu = Mmu.instance
    lateinit var cartridge: Cartridge
    private var running = false
    var paused = false

    init {
        reset()

        if (cart != null) {
            loadCartridge(cart)
        }
    }

    fun reset() {
        running = false
        paused = false
        mmu.reset()
        cpu.reset()
    }

    fun step() {
        cpu.step()
    }

    fun togglePause() {
        paused = !paused
    }

    fun stop() {
        running = false
    }

    override fun run() {
        running = true
        while (running) {
            if (!paused) {
                step()
            }
        }
    }

    fun loadCartridge(cart: File) {
        cartridge = Cartridge(cart)
        mmu.cartridge = cartridge
        reset()
    }
}