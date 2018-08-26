import cpu.Cpu
import memory.Mmu
import memory.cartridge.Cartridge
import utils.Log
import java.io.File

class GameBoy(cart: File) {
    val cpu = Cpu()
    private var cartridge = Cartridge(cart)
    val mmu = Mmu.instance

    init {
        reset()
    }

    fun reset() {
        mmu.cartridge = cartridge
        mmu.reset()
        cpu.reset()
    }

    fun frame() {
        val start = cpu.registers.clock
        try {
            while (cpu.registers.clock - start < 70224) {
                step()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Exception occured")
        }
    }

    fun step() {
        cpu.step()
    }

    fun loadCartridge(cart: File) {
        val temp = Cartridge(cart)
        cartridge = temp
        reset()
    }
}