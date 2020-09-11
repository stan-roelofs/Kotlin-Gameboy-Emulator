package gui

import gameboy.utils.Log
import java.awt.Window
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.zip.ZipFile
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

class RomChooser {
    private var lastLocation: File? = null
    private val fileChooser = JFileChooser()

    init {
        fileChooser.dialogTitle = "Choose rom"
        fileChooser.addChoosableFileFilter(FileNameExtensionFilter("Roms", "gb", "gbc", "zip"))
    }

    fun chooseRom(parentWindow: Window?): File? {
        if (lastLocation != null) {
            fileChooser.currentDirectory = lastLocation
        }

        val result = fileChooser.showOpenDialog(parentWindow)
        if (result == JFileChooser.APPROVE_OPTION) {
            var file = fileChooser.selectedFile
            lastLocation = file.parentFile

            when (file.extension) {
                "zip" -> {
                    val zipFile = ZipFile(file.path)

                    val entries = zipFile.entries()
                    while(entries.hasMoreElements()) {
                        val entry = entries.nextElement()
                        if (entry.name.endsWith(".gb")) {
                            val inputStream = zipFile.getInputStream(entry)
                            val newPath = "${file.parentFile.path}/${entry.name}"

                            Files.copy(inputStream, Paths.get(newPath), StandardCopyOption.REPLACE_EXISTING)
                            file = File(newPath)
                            return file
                        }
                    }
                }
                "gbc",
                "gb" -> {
                    return file
                }
                else -> {
                    Log.e("Unknown file type, choose a .gb file or a .zip file")
                }
            }
        }

        return null
    }
}
