package nl.stanroelofs.gameboy

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat.requestPermissions
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import gameboy.GameBoy
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AndroidLauncher : AndroidApplication() {

    private var app : Androidlol? = null
    private var gb : GameBoy? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        val am = assets
        val input = am.open("zelda.gb")
        val file = File.createTempFile("adadada", "b")
        copyStreamToFile(input, file)
        gb = GameBoy(file)
        app = Androidlol(gb!!, this)
        initialize(app, config)
        app!!.startgb()
        requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> {
                val fileUri = data?.data
                var path = fileUri!!.path
                path = path!!.replace("document/raw:", "")
                val file = File(path)

                app?.stopgb()
                gb?.loadCartridge(file)
                app?.startgb()
            }
        }
    }
}
