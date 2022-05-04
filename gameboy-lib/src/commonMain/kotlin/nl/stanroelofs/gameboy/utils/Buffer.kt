package nl.stanroelofs.gameboy.utils

class Buffer<T> : Iterable<T> {

    private val buffer : MutableList<T>

    constructor() {
        buffer = ArrayList()
    }

    constructor(values: List<T>) {
        buffer = values.toMutableList()
    }

    constructor(values: Array<T>) {
        buffer = values.toMutableList()
    }

    fun reset() {
        buffer.clear()
    }

    fun put(value: T) {
        buffer.add(value)
    }

    operator fun get(index: Int) : T {
        return buffer[index]
    }

    fun length() : Int {
        return buffer.size
    }

    fun toList() : List<T> {
        return buffer
    }

    override fun iterator(): Iterator<T> {
        return buffer.iterator()
    }

    override fun equals(other: Any?): Boolean {
        return buffer == other
    }
}