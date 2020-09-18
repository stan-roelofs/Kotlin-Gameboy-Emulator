package gameboy

import gameboy.cpu.Cpu
import gameboy.cpu.RegistersCGB
import gameboy.memory.MmuCGB
import gameboy.memory.cartridge.Cartridge

class GameBoyCGB(cartridge: Cartridge) : GameBoy() {
    override val mmu = MmuCGB(cartridge)
    override val cpu = Cpu(mmu, RegistersCGB())

    init {
        reset()
    }
}