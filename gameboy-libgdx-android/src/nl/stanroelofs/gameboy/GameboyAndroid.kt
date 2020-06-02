package nl.stanroelofs.gameboy

import GameboyLibgdx
import SoundOutputGdx
import android.app.Activity
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import gameboy.GameBoy
import memory.io.sound.SoundOutput

class Androidlol(gb: GameBoy, private val context: Activity) : GameboyLibgdx(gb) {

    override var output: SoundOutput = SoundOutputGdx()
    private lateinit var controller: Controller
    private lateinit var shape: ShapeRenderer
    private lateinit var font: BitmapFont

    override fun create() {
        super.create()
        controller = Controller(gb, context)
        shape = ShapeRenderer()
        font = BitmapFont()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        controller.resize(width, height)
    }

    override fun render() {
        viewport.apply()
        super.render()
        controller.render(shape)

        batch?.begin()
        font.draw(batch, "${fpsCounter.FPS}", 10f, 20f)
        batch?.end()
    }
}
