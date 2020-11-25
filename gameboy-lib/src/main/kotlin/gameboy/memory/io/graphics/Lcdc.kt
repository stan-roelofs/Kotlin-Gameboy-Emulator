package gameboy.memory.io.graphics

import gameboy.utils.getBit

class Lcdc {

    var LcdcByte = 0

    fun getLcdEnable(): Boolean {
        return LcdcByte.getBit(7)
    }

    fun getWindowTileMap(): Boolean {
        return LcdcByte.getBit(6)
    }

    fun getWindowEnable(): Boolean {
        return LcdcByte.getBit(5)
    }

    fun getTileDataSelect(): Boolean {
        return LcdcByte.getBit(4)
    }

    fun getBackgroundTileMap(): Boolean {
        return LcdcByte.getBit(3)
    }

    fun getObjectSize(): Boolean {
        return LcdcByte.getBit(2)
    }

    fun getObjectEnable(): Boolean {
        return LcdcByte.getBit(1)
    }

    fun getBGWindowDisplay(): Boolean {
        return LcdcByte.getBit(0)
    }
}