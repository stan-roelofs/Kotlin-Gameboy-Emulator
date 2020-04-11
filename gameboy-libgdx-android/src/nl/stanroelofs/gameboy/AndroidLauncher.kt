package nl.stanroelofs.gameboy

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import GameboyLibgdx
import GameBoy
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()

        val am = assets
        val input = am.open("Legend of Zelda, The - Link's Awakening (U) (V1.2) [!].gb")
        val file = File.createTempFile("adadada", "b")
        copyStreamToFile(input, file)
        val gb = GameBoy(file)
        val app = GameboyLibgdx(gb)
        initialize(app, config)
        app.startgb()
    }
}

fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
    inputStream.use { input ->
        val outputStream = FileOutputStream(outputFile)
        outputStream.use { output ->
            val buffer = ByteArray(4 * 1024) // buffer size
            while (true) {
                val byteCount = input.read(buffer)
                if (byteCount < 0) break
                output.write(buffer, 0, byteCount)
            }
            output.flush()
        }
    }
}