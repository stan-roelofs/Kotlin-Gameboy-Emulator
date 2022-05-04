package nl.stanroelofs.gameboy.utils

val Number.firstByte: Int
    get() = this.toInt() and 0xff

val Number.secondByte: Int
    get() = (this.toInt() shr 8) and 0xff

fun Int.setSecondByte(value: Int): Int {
    return ((value and 0xff) shl 8) or (this and 0xff)
}

fun Int.setFirstByte(value: Int): Int {
    return (this and 0xff00) or (value and 0xff)
}

fun Int.getBit(pos: Int): Boolean {
    return (this and (1 shl pos)) != 0
}

fun Int.setBit(pos: Int, state: Boolean) : Int {
    return if (state) {
        setBit(pos)
    } else {
        clearBit(pos)
    }
}

fun Int.setBit(pos: Int): Int {
    return (this or (1 shl pos)) and 0xff
}

fun Int.clearBit(pos: Int): Int {
    return this and ((1 shl pos).inv() and 0xff)
}

fun Number.toHexString(): String {
    return this.toHexString(4)
}

fun Number.toHexString(num: Int): String {
    return "0x%0${num}X$this"
}