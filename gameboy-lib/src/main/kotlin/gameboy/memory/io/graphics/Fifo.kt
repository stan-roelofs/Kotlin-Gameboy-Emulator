package gameboy.memory.io.graphics

@Suppress("UNCHECKED_CAST")
/**
 * A generic FIFO queue implementation which can hold up to [size] elements
 * @param E the type of members in this FIFO
 * @param size the maximum number of elements the queue can hold
 * @constructor Constructs an empty FIFO of a certain size
 */
class Fifo<E : Any?>(size : Int) {

    private val values = Array<Any?> (size) { null }
    private var index = 0

    /**
     * Returns true when the FIFO is empty (size == 0), false otherwise
     */
    val empty : Boolean
        get() = size == 0

    val full : Boolean
        get() = size == maxSize

    /**
     * The number of elements in the FIFO
     */
    var size = 0
        private set

    /**
     * The maximum number of elements that fit in the FIFO
     */
    val maxSize = size

    /**
     * Removes all elements from the FIFO
     */
    fun clear() {
        index = 0
        size = 0
    }

    /**
     * Removes the first element from the FIFO
     * @return The element which was removed
     * @throws NoSuchElementException if the FIFO is empty
     */
    fun pop(): E {
        if (empty)
            throw NoSuchElementException("FIFO is empty")

        size--
        val value = values[index++]
        index %= values.size

        return value as E
    }

    /**
     * Pushes an element to the front of the FIFO
     * @throws IllegalStateException when the FIFO is full
     */
    fun push(value: E) {
        if (full)
            throw IllegalStateException("FIFO is full")

        values[(index + size) % values.size] = value
        size++
    }

    /**
     * Returns the first element in the FIFO without removing it from the FIFO
     * @return The element at the front of the FIFO
     */
    fun peek(index: Int): E {
        return values[(this.index + index) % values.size] as E
    }
}