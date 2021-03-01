package nl.stanroelofs.gameboy

import GameboyLibgdx
import GameboyThread
import SoundOutputGdx
import android.app.Activity
import android.os.Process.THREAD_PRIORITY_URGENT_DISPLAY
import android.os.Process.setThreadPriority
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import nl.stanroelofs.gameboy.memory.io.sound.SoundOutput

class GameboyAndroid(private val context: Activity) : GameboyLibgdx() {

    override var output: SoundOutput = SoundOutputGdx()
    private var controller: Controller? = null
    private lateinit var shape: ShapeRenderer

    override fun startgb(gb: GameBoy) {
        if (gbThread?.running == true)
            stopgb()

        gameboy = gb
        gb.mmu.io.sound.output = output
        gb.mmu.io.ppu.lcd.addListener(this)
        controller = Controller(gb, context)
        gbThread = AGameboyThread(gb)
        gbThread!!.start()
    }

    override fun create() {
        super.create()
        controller = Controller(gameboy, context)
        shape = ShapeRenderer()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        controller?.resize(width, height)
    }

    override fun render() {
        viewport.apply()
        super.render()
        controller?.render(shape)
    }
}

class AGameboyThread(gb: GameBoy) : GameboyThread(gb) {
    init {
        setThreadPriority(THREAD_PRIORITY_URGENT_DISPLAY)
    }
}
