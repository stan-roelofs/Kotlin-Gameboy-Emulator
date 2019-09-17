package gui

import javafx.stage.FileChooser
import javafx.stage.Window
import java.io.File

class RomChooser {
    private var lastLocation: File? = null
    private val fileChooser = FileChooser()

    init {
        fileChooser.title = "Choose rom"
        fileChooser.selectedExtensionFilter = FileChooser.ExtensionFilter("Roms", "*.gb")
    }

    fun chooseRom(parentWindow: Window?): File? {
        if (lastLocation != null) {
            fileChooser.initialDirectory = lastLocation
        }

        val file = fileChooser.showOpenDialog(parentWindow)

        if (file != null) {
            lastLocation = file.parentFile
        }

        return file
    }
}
