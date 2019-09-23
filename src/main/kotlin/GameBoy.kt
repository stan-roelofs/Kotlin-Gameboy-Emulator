import cpu.Cpu
import memory.Mmu
import memory.cartridge.Cartridge
import java.io.File

class GameBoy(cart: File?) {
    val cpu = Cpu()
    val mmu = Mmu.instance
    lateinit var cartridge: Cartridge

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

    fun loadCartridge(cart: File) {
        cartridge = Cartridge(cart)
        mmu.cartridge = cartridge
        reset()
    }
}