package utils

object Log {
    var LOG = true

    fun i(string: String) {
        if (LOG) log("INFO: $string")
    }

    fun e(string: String) {
        if (LOG) log("ERROR: $string")
    }

    fun d(string: String) {
        if (LOG) log("DEBUG: $string")
    }

    fun w(string: String) {
        if (LOG) this.log("WARNING: $string")
    }

    private fun log(string: String) {
        println(string)
    }
}