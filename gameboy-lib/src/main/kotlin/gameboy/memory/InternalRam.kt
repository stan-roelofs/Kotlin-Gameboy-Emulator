package gameboy.memory

import gameboy.utils.toHexString

abstract class InternalRam : Memory {
    protected fun checkAddress(address: Int) {
        if (address < 0xC000 || address >= 0xFE00) {
            throw IllegalArgumentException("Address ${address.toHexString()} does not belong to InternalRam")
        }
    }
}