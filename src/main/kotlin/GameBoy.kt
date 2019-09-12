import cpu.Cpu
import memory.Mmu
import memory.cartridge.Cartridge
import java.io.File

class GameBoy(cart: File?) {
    val cpu = Cpu()
    private lateinit var cartridge: Cartridge
    val mmu = Mmu.instance

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
        val temp = Cartridge(cart)
        cartridge = temp
        mmu.cartridge = cartridge
        reset()
    }
}