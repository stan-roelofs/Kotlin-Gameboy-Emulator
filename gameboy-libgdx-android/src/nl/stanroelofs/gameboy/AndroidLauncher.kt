package nl.stanroelofs.gameboy

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import GameboyLibgdx
import gameboy.GameBoy
import android.util.Log
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import memory.io.Joypad
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class Androidlol(gb: GameBoy) : GameboyLibgdx(gb) {

    private val controls = ArrayList<Button>()

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

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()

        val am = assets
        val input = am.open("Pokemon Red.gb")
        val file = File.createTempFile("adadada", "b")
        copyStreamToFile(input, file)
        val gb = GameBoy(file)
        val app = Androidlol(gb)
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

class Button(val x: Float, val y: Float, val key: Joypad.JoypadKey) {
    var width = 15f
    var height = 15f

    fun Pressed(x: Float, y: Float): Boolean {
        return (x >= this.x && x <= this.x + width) && (y >= this.y && y <= this.y + width)
    }
}