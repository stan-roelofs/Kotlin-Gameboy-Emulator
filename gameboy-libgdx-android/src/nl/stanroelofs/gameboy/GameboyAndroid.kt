package nl.stanroelofs.gameboy

import GameboyLibgdx
import SoundOutputGdx
import android.app.Activity
import android.os.Process.THREAD_PRIORITY_URGENT_DISPLAY
import android.os.Process.setThreadPriority
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import gameboy.GameBoy
import memory.io.sound.SoundOutput

class GameboyAndroid(gb: GameBoy, private val context: Activity) : GameboyLibgdx(gb) {

    override var output: SoundOutput = SoundOutputGdx()
    private lateinit var controller: Controller
    private lateinit var shape: ShapeRenderer

    override fun startgb() {
        gbThread = GameboyThread(gb)
        gbThread.start()
    }

    override fun create() {
        super.create()
        controller = Controller(gb, context)
        shape = ShapeRenderer()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        controller.resize(width, height)
    }

    override fun render() {
        viewport.apply()
        super.render()
        controller.render(shape)
    }
}

class GameboyThread(gb: GameBoy) : Thread(gb) {
    init {
        setThreadPriority(THREAD_PRIORITY_URGENT_DISPLAY)
    }
}
