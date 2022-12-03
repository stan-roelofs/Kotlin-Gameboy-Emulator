package nl.stanroelofs.gameboy.memory

import nl.stanroelofs.gameboy.utils.toHexString

abstract class InternalRam : Memory {
    protected fun checkAddress(address: Int) {
        if (address < 0xC000 || address >= 0xFE00) {
            throw IllegalArgumentException("Address ${address.toHexString()} does not belong to InternalRam")
        }
    }
}