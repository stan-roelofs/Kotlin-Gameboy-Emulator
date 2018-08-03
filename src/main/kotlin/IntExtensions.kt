fun Int.getFirstByte(): Int {
    return (this and 0xff)
}

fun Int.getSecondByte(): Int {
    return (this shr 8)
}

fun setSecondByte(num: Int, value: Int): Int {
    return ((value and 0xff) shl 8) or (num and 0xff)
}

fun setFirstByte(num: Int, value: Int): Int {
    return (num and 0xff00) or (value and 0xff)
}

fun Int.getBit(pos: Int): Boolean {
    return (this and (1 shl pos)) != 0
}

fun setBit(num: Int, pos: Int, state: Boolean): Int {
    return if (state) {
        setBit(num, pos)
    } else {
        clearBit(num, pos)
    }
}

fun setBit(num: Int, pos: Int): Int {
    return (num or (1 shl pos)) and 0xff
}

fun clearBit(num: Int, pos: Int): Int {
    return num and ((1 shl pos).inv() and 0xff)
}