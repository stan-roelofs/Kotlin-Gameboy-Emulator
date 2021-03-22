import nl.stanroelofs.minilog.Logging

class FpsCounter {
    private var startTime = 0L
    var FPS = 0.0
        private set
    private var frameCount = 0

    private val logger = Logging.getLogger(FpsCounter::class.java.name)

    fun frameRendered() {
        ++frameCount

        val newTime = System.nanoTime()
        val delta = newTime - startTime
        if (delta > 1e9) {
            startTime = newTime
            FPS = frameCount / (delta / 1e9)
            frameCount = 0
            logger.d{"$FPS"}
        }
    }
}