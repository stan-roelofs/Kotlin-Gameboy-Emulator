package gui

import javafx.stage.FileChooser
import javafx.stage.Window
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipFile

class RomChooser {
    private val extensionsList = listOf("*.gb", "*.zip")
    private var lastLocation: File? = null
    private val fileChooser = FileChooser()

    init {
        fileChooser.title = "Choose rom"
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Roms", extensionsList))
    }

    fun chooseRom(parentWindow: Window?): File? {
        if (lastLocation != null) {
            fileChooser.initialDirectory = lastLocation
        }

        var file = fileChooser.showOpenDialog(parentWindow)
        if (file != null) {
            lastLocation = file.parentFile

            if (file.extension == "zip") {
                val zipFile = ZipFile(file.path)

                val entries = zipFile.entries()
                while(entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    if (entry.name.endsWith(".gb")) {
                        val inputStream = zipFile.getInputStream(entry)
                        val newPath = "${file.parentFile.path}/${entry.name}"
                        Files.copy(inputStream, Paths.get(newPath))
                        file = File(newPath)
                    }
                }
            }
        }

        return file
    }
}
