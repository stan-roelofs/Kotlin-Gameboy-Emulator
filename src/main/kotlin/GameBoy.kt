import cpu.Cpu
import java.io.File
import java.util.logging.Logger

class GameBoy(cart: File) {

    companion object {
        val logger = Logger.getLogger("GameBoy")!!
    }

    val cpu = Cpu()
    val gpu = Gpu(cpu.registers)
    val cart = Cartridge(cart)
    val mmu = Mmu.instance

    init {
        reset()
    }

    fun reset() {
        mmu.cartridge = cart
        mmu.reset()
        gpu.reset()
        cpu.reset()
    }

    fun step() {
        cpu.step()
        gpu.step()
    }
}