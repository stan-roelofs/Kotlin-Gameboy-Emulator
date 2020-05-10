class FpsCounter {
    private var oldTime = 0L
    var FPS = 0
        private set
    private var framesCounter = 0

    fun frameRendered() {
        framesCounter++;

        val newTime = System.nanoTime()
        val delta = newTime - oldTime
        if (delta > 1000000000) {
            oldTime = newTime
            FPS = framesCounter
            framesCounter = 0
        }
    }
}