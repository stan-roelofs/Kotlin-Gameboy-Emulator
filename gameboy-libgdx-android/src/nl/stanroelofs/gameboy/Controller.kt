package nl.stanroelofs.gameboy

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import gameboy.GameBoy
import gameboy.memory.io.Joypad
import gameboy.utils.Log
import java.io.File
import java.io.FileNotFoundException
import javax.microedition.khronos.opengles.GL10

class Controller(gb: GameBoy?, activity: Activity) {
    private val camera: Camera = OrthographicCamera()
    private val viewPort: Viewport = ExtendViewport(100f, 100f, camera)
    private val controls: ArrayList<Button> = ArrayList()

    private val inputProcessor = object : InputAdapter() {
        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            for (control in controls) {
                val gamecoordinates = viewPort.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                if (control.isPressed(gamecoordinates.x, gamecoordinates.y)) {
                    control.onReleased()
                }
            }
            return true
        }

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            for (control in controls) {
                val gamecoordinates = viewPort.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
                if (control.isPressed(gamecoordinates.x, gamecoordinates.y)) {
                    control.onPressed()
                }
            }
            return true
        }
    }

    init {
        Gdx.input.inputProcessor = inputProcessor

        controls.add(ControllerButton(80f, 30f, gb, Joypad.JoypadKey.A))
        controls.add(ControllerButton(70f, 10f, gb, Joypad.JoypadKey.B))
        controls.add(ControllerButton(15f, 25f, gb, Joypad.JoypadKey.UP))
        controls.add(ControllerButton(5f, 15f, gb, Joypad.JoypadKey.LEFT))
        controls.add(ControllerButton(25f, 15f, gb, Joypad.JoypadKey.RIGHT))
        controls.add(ControllerButton(15f, 5f, gb, Joypad.JoypadKey.DOWN))
        controls.add(ControllerButton(50f, 10f, gb, Joypad.JoypadKey.SELECT))
        controls.add(ControllerButton(50f, 10f, gb, Joypad.JoypadKey.START))
        controls.add(MenuButton(50f, 50f, gb, activity))

        positionControls()
    }

    fun render(shape: ShapeRenderer) {
        viewPort.apply()
        shape.projectionMatrix = camera.combined

        for (control in controls) {
            control.draw(shape)
        }
    }

    fun resize(width: Int, height: Int) {
        viewPort.update(width, height, true)
        positionControls()
    }

    private fun positionControls() {
        controls[0].x = viewPort.worldWidth - 20
        controls[1].x = viewPort.worldWidth - 40
        controls[6].x = (viewPort.worldWidth / 2) - 15
        controls[7].x = (viewPort.worldWidth / 2) + 5
    }
}

abstract class Button(var x: Float, var y: Float, protected val gb: GameBoy?) {
    abstract fun isPressed(x: Float, y: Float): Boolean
    abstract fun onPressed()
    abstract fun onReleased()
    abstract fun draw(shapeRenderer: ShapeRenderer)
}

class ControllerButton(x: Float, y: Float, gb: GameBoy?, private val key: Joypad.JoypadKey) : Button(x, y, gb) {
    private val width = 10f
    private val height = 10f

    override fun isPressed(x: Float, y: Float): Boolean {
        return (x >= this.x && x <= this.x + width) && (y >= this.y && y <= this.y + width)
    }

    override fun onPressed() {
        gb?.mmu?.io?.joypad?.keyPressed(key)
    }

    override fun onReleased() {
        gb?.mmu?.io?.joypad?.keyReleased(key)
    }

    override fun draw(shapeRenderer: ShapeRenderer) {
        Gdx.gl.glEnable(GL10.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color(0f, 0f, 0f, 0.3f)
        shapeRenderer.rect(x, y, width, height)
        shapeRenderer.end()
    }
}

class MenuButton(x: Float, y: Float, gb: GameBoy?, private val context: Activity) : Button(x, y, gb) {
    private val width = 20f
    private val height = 15f

    override fun isPressed(x: Float, y: Float): Boolean {
        return (x >= this.x && x <= this.x + width) && (y >= this.y && y <= this.y + width)
    }

    override fun onPressed() {
        context.runOnUiThread {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Menu")

            val items: Array<CharSequence> = arrayOf("Load rom", "Pause", "Save", "Load")
            alertDialog.setItems(items) {
                _, i ->
                run {
                    when (i) {
                        0 -> {
                            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
                            chooseFile.type = "*/*"
                            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
                            context.startActivityForResult(chooseFile, 0)
                        }
                        1 -> {
                            gb?.togglePause()
                        }
                        2 -> {
                            val root = File(context.filesDir, "Saves")
                            if (!root.exists()) {
                                if (!root.mkdirs())
                                    Log.e("Could not create directory")
                            }

                            if (root.exists()) {
                                val saveFile = File(root, "${gb?.mmu?.cartridge?.cartridgeFile?.nameWithoutExtension}.sav")
                                if (!saveFile.exists())
                                    saveFile.createNewFile()

                                try {
                                    gb?.mmu?.cartridge?.saveRam(saveFile)
                                } catch (e: FileNotFoundException) {
                                    e.printStackTrace()
                                }
                            } else
                                null
                        }
                        3 -> {
                            val root = File(context.filesDir, "Saves")
                            val saveFile = File(root, "${gb?.mmu?.cartridge?.cartridgeFile?.nameWithoutExtension}.sav")
                            if (saveFile.exists())
                                gb?.mmu?.cartridge?.loadRam(saveFile)
                            else
                                null
                        }
                        else -> {

                        }
                    }

                }
            }
            alertDialog.show()
        }
    }

    override fun onReleased() {

    }

    override fun draw(shapeRenderer: ShapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.BLACK
        shapeRenderer.rect(x, y, width, height)
        shapeRenderer.end()
    }
}