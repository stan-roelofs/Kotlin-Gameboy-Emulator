import cpu.Cpu
import memory.Cartridge
import memory.Mmu
import java.io.File

class GameBoy(cart: File) {
    val cpu = Cpu()
    private val cart = Cartridge(cart)
    val mmu = Mmu.instance

    init {
        reset()
    }

    fun reset() {
        mmu.cartridge = cart
        mmu.reset()
        cpu.reset()
    }

    fun step() {
        cpu.step()
    }
}