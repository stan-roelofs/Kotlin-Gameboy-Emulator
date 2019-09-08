package memory.io.sound

class LengthCounter(private var fullLength: Int) {

    private val DIVIDER = 4194304 / 256

    private var length: Int = 0

    private var i: Long = 0

    private var enabled: Boolean = false

    fun start() {
        i = 8192
    }

    fun tick() {
        if (++i == DIVIDER.toLong()) {
            i = 0
            if (enabled && length > 0) {
                length--
            }
        }
    }

    fun setLength(length: Int) {
        if (length == 0) {
            this.length = fullLength
        } else {
            this.length = length
        }
    }

    fun setNr4(value: Int) {
        val enable = value and (1 shl 6) != 0
        val trigger = value and (1 shl 7) != 0

        if (enabled) {
            if (length == 0 && trigger) {
                if (enable && i < DIVIDER / 2) {
                    setLength(fullLength - 1)
                } else {
                    setLength(fullLength)
                }
            }
        } else if (enable) {
            if (length > 0 && i < DIVIDER / 2) {
                length--
            }
            if (length == 0 && trigger && i < DIVIDER / 2) {
                setLength(fullLength - 1)
            }
        } else {
            if (length == 0 && trigger) {
                setLength(fullLength)
            }
        }
        this.enabled = enable
    }

    fun getValue(): Int {
        return length
    }

    fun isEnabled(): Boolean {
        return enabled
    }

    override fun toString(): String {
        return String.format("LengthCounter[l=%d,f=%d,c=%d,%s]", length, fullLength, i, if (enabled) "enabled" else "disabled")
    }

    internal fun reset() {
        this.enabled = true
        this.i = 0
        this.length = 0
    }
}