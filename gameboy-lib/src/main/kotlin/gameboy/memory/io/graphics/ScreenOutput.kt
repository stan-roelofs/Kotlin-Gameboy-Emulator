package gameboy.memory.io.graphics

/** Interface for a class which acts as a screen output */
interface ScreenOutput {

    /**
     * This function is called when rendering finishes (v-blank)
     * Implement this function to render the screenbuffer to some output
     * @param screenBuffer screen buffer
     */
    fun render(screenBuffer: ByteArray)
}