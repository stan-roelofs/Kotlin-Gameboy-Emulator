package gameboy

import gameboy.cpu.CpuDMG
import gameboy.memory.MmuDMG
import gameboy.memory.cartridge.Cartridge

class GameBoyDMG(cartridge: Cartridge) : GameBoy() {
    override val mmu = MmuDMG(cartridge)
    override val cpu = CpuDMG(mmu)

    init {
        reset()
    }
}