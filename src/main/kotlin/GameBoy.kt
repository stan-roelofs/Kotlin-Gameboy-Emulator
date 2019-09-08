import cpu.Cpu
import memory.Mmu
import memory.cartridge.Cartridge
import utils.Log
import java.io.File

class GameBoy(cart: File?) {

    companion object {
        val TICKS_PER_SEC = 4194304
    }

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
        mmu.cartridge = cartridge
        reset()
    }
}