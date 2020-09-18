package gameboy.memory

import gameboy.memory.cartridge.Cartridge
import gameboy.memory.io.IO

class MmuCGB(cartridge: Cartridge) : Mmu(cartridge) {
    override val hram: HRam
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val internalRam: InternalRam
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val oam: Oam
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val io: IO
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun dmaReadByte(address: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dmaWriteByte(address: Int, value: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readByte(address: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeByte(address: Int, value: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}