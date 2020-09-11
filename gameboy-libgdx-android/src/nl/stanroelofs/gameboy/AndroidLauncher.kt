package nl.stanroelofs.gameboy

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat.requestPermissions
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import gameboy.GameBoy
import gameboy.memory.cartridge.Cartridge
import java.io.File

class AndroidLauncher : AndroidApplication() {

    private lateinit var app : GameboyAndroid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        app = GameboyAndroid(this)
        initialize(app, config)
        requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), 1)
    }

    override fun onPause() {
        super.onPause()
        //gb!!.paused = true
    }

    override fun onResume() {
        super.onResume()
        //gb!!.paused = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            0 -> {
                val fileUri = data?.data
                var path = fileUri!!.path
                path = path!!.replace("document/raw:", "")
                val file = File(path)

                if (!file.exists() || !file.canRead())
                    return

                val gb = GameBoy(Cartridge(file))
                app.startgb(gb)
            }
        }
    }
}
