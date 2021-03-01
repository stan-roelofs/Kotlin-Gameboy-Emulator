package nl.stanroelofs.gameboy.memory.io.graphics

/** Interface for a class which is interested in a vsync event */
interface VSyncListener {

    /**
     * This function is called when rendering finishes (v-blank)
     * Implement this function to render the screen buffer to some output
     * @param screenBuffer screen buffer
     */
    fun vsync(screenBuffer: ByteArray)
}