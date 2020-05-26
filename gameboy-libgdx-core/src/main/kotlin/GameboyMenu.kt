import java.io.File

/**
 * Declares an interface for functions which should be implemented in a menu for the gameboy GUI
 */
interface GameboyMenu {
    /**
     * Loads [file] as a ROM into the gameboy, and starts the emulator
     * @param file The ROM file
     * @return true if loading succeeds, false otherwise
     */
    fun loadRom(file: File): Boolean

    /**
     * Pauses / unpauses the emulator
     */
    fun togglePause()

    /**
     * Resets the emulator. Equivalent to turning the gameboy on and off.
     */
    fun reset()
}