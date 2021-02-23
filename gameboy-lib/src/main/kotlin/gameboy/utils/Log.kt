package gameboy.utils

object Log {
    enum class Level {
        NONE,
        ERROR,
        WARNING,
        INFO,
        DEBUG
    }

    var level = Level.DEBUG

    fun i(string: String) {
        if (level <= Level.INFO) log("INFO: $string")
    }

    fun e(string: String) {
        if (level <= Level.ERROR) log("ERROR: $string")
    }

    fun d(string: String) {
        if (level <= Level.DEBUG) log("DEBUG: $string")
    }

    fun w(string: String) {
        if (level <= Level.WARNING) log("WARNING: $string")
    }

    private fun log(string: String) {
        println(string)
    }
}