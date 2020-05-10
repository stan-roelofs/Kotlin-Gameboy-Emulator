package nl.stanroelofs.gameboy

import GameboyLibgdx
import SoundOutputGdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import gameboy.GameBoy
import memory.io.Joypad
import memory.io.sound.SoundOutput
import nl.stanroelofs.gameboy.Button

class Androidlol(gb: GameBoy) : GameboyLibgdx(gb), InputProcessor {

    private val controls = ArrayList<Button>()
    override var output: SoundOutput = SoundOutputGdx()

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val gamecoordinates = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
        for (control in controls) {
            if (control.Pressed(gamecoordinates.x, gamecoordinates.y)) {
                gb.mmu.io.joypad.keyPressed(control.key)
            }
        }
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val gamecoordinates = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
        for (control in controls) {
            if (control.Pressed(gamecoordinates.x, gamecoordinates.y)) {
                gb.mmu.io.joypad.keyReleased(control.key)
            }
        }
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return true
    }

    override fun keyTyped(character: Char): Boolean {
        return true
    }

    override fun scrolled(amount: Int): Boolean {
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        return true
    }

    private lateinit var shape: ShapeRenderer

    override fun create() {
        super.create()
        shape = ShapeRenderer()

        controls.add(Button(120f, 50f, Joypad.JoypadKey.A))
        controls.add(Button(110f, 20f, Joypad.JoypadKey.B))
        controls.add(Button(30f, 50f, Joypad.JoypadKey.UP))
        controls.add(Button(15f, 35f, Joypad.JoypadKey.LEFT))
        controls.add(Button(45f, 35f, Joypad.JoypadKey.RIGHT))
        controls.add(Button(30f, 20f, Joypad.JoypadKey.DOWN))
    }

    override fun render() {
        super.render()
        shape.projectionMatrix = cam.combined

        for (control in controls) {
            shape.begin(ShapeRenderer.ShapeType.Line)
            shape.color = Color.BLACK
            shape.rect(control.x, control.y, control.width, control.height)
            shape.end()
        }
    }
}