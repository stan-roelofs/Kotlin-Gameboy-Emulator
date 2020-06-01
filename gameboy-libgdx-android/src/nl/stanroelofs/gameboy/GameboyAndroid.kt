package nl.stanroelofs.gameboy

import GameboyLibgdx
import SoundOutputGdx
import android.app.Activity
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import gameboy.GameBoy
import memory.io.sound.SoundOutput

class Androidlol(gb: GameBoy, private val context: Activity) : GameboyLibgdx(gb) {

    override var output: SoundOutput = SoundOutputGdx()
    private lateinit var controller: Controller
    private lateinit var shape: ShapeRenderer

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
