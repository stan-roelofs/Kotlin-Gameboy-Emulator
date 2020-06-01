package nl.stanroelofs.gameboy

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat.requestPermissions
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import gameboy.GameBoy
import java.io.File

class AndroidLauncher : AndroidApplication() {

    private var app : Androidlol? = null
    private var gb : GameBoy? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        gb = GameBoy()
        app = Androidlol(gb!!, this)
        initialize(app, config)

        requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
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
