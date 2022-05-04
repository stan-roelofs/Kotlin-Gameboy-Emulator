package nl.stanroelofs.gameboy.memory.io.graphics

import nl.stanroelofs.gameboy.memory.Mmu
import nl.stanroelofs.gameboy.memory.Register
import nl.stanroelofs.gameboy.utils.getBit

class Lcdc : Register(Mmu.LCDC) {

    fun getLcdEnable(): Boolean {
        return value.getBit(7)
    }

    fun getWindowTileMap(): Boolean {
        return value.getBit(6)
    }

    fun getWindowEnable(): Boolean {
        return value.getBit(5)
    }

    fun getTileDataSelect(): Boolean {
        return value.getBit(4)
    }

    fun getBackgroundTileMap(): Boolean {
        return value.getBit(3)
    }

    fun getObjectSize(): Boolean {
        return value.getBit(2)
    }

    fun getObjectEnable(): Boolean {
        return value.getBit(1)
    }

    fun getBGWindowDisplay(): Boolean {
        return value.getBit(0)
    }
}