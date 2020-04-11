import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.StretchViewport
import memory.io.Joypad

open class GameboyLibgdx(protected val gb: GameBoy) : ApplicationAdapter(), InputProcessor {
    val color0 = Color(224f / 255, 248f / 255, 208f / 255, 1.0f)
    val color1 = Color(136f / 255, 192f / 255, 112f / 255, 1.0f)
    val color2 = Color(52f / 255, 104f / 255, 86f / 255, 1f)
    val color3 = Color(8f / 255, 24f / 255, 32f / 255, 1f)
    val colors = arrayOf(color0, color1, color2, color3)

    private var batch : SpriteBatch? = null
    private var output : SoundOutputGdx? = null
    private var gbThread = Thread(gb)
    private val cam = OrthographicCamera()
    private val viewport = StretchViewport(160f, 144f, cam)

    fun startgb() {
        gbThread = Thread(gb)
        gbThread.start()
    }

    fun stopgb() {
        gb.stop()
        gbThread.join()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun create() {
        batch = SpriteBatch()
        Gdx.input.inputProcessor = this
        output = SoundOutputGdx()
        gb.mmu.io.sound.output = output
        cam.position.set(80f, 72f, 0f)
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val pixmap = Pixmap(160, 144, Pixmap.Format.RGB888)
        for (y in 0 until 144) {
            for (x in 0 until 160) {
                pixmap.setColor(colors[gb.mmu.io.lcd.screenBuffer[y][x]])
                pixmap.drawPixel(x, y)
            }
        }

        val img = Texture(pixmap)

        batch?.projectionMatrix = cam.combined
        batch?.begin()
        batch?.draw(img, 0f, 0f, 160f, 144f)
        batch?.end()
        Gdx.graphics.setTitle("${Gdx.graphics.framesPerSecond}")
    }

    override fun dispose() {
        batch?.dispose()
        output?.dispose()
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
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
        when (keycode) {
            Input.Keys.ENTER -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.START)
            Input.Keys.LEFT -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.LEFT)
            Input.Keys.RIGHT -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.RIGHT)
            Input.Keys.UP -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.UP)
            Input.Keys.DOWN -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.DOWN)
            Input.Keys.Z -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.A)
            Input.Keys.X -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.B)
            Input.Keys.TAB -> gb.mmu.io.joypad.keyReleased(Joypad.JoypadKey.SELECT)
            else -> {}
        }
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return true
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.ENTER -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.START)
            Input.Keys.LEFT -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.LEFT)
            Input.Keys.RIGHT -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.RIGHT)
            Input.Keys.UP -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.UP)
            Input.Keys.DOWN -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.DOWN)
            Input.Keys.Z -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.A)
            Input.Keys.X -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.B)
            Input.Keys.TAB -> gb.mmu.io.joypad.keyPressed(Joypad.JoypadKey.SELECT)
            else -> {}
        }
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }
}