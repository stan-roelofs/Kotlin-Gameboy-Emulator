package gameboy

import gameboy.cpu.Cpu
import gameboy.cpu.RegistersDMG
import gameboy.memory.MmuDMG
import gameboy.memory.cartridge.Cartridge

class GameBoyDMG(cartridge: Cartridge) : GameBoy() {
    override val mmu = MmuDMG(cartridge)
    override val cpu = Cpu(mmu, RegistersDMG())

    init {
        reset()
    }
}