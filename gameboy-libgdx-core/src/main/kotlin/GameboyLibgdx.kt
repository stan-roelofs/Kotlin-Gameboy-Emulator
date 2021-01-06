import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.StretchViewport
import gameboy.GameBoy
import gameboy.memory.io.graphics.ScreenOutput
import gameboy.memory.io.sound.SoundOutput
import java.nio.ByteBuffer

/** Base libgdx gameboy class
 *
 * Contains main rendering logic and platform independent functionality
 */
abstract class GameboyLibgdx : ApplicationAdapter(), ScreenOutput {

    val width = GameBoy.SCREEN_WIDTH
    val height = GameBoy.SCREEN_HEIGHT

    private lateinit var batch : SpriteBatch
    private lateinit var screenTexture: Texture

    /** Sound output object, required by gameboy */
    abstract var output : SoundOutput
    var gameboy: GameBoy? = null
        protected set

    protected lateinit var gbThread: Thread
    protected val cam = OrthographicCamera()
    protected val viewport = StretchViewport(width.toFloat(), height.toFloat(), cam)
    private var fpsCounter = FpsCounter()

    private val screenBufferArray = ByteArray(width * height * 3)
    private val buffer = ByteBuffer.allocateDirect(width * height * 3)

    open fun startgb(gb: GameBoy) {
        if (gameboy?.running == true)
            stopgb()

        gameboy = gb
        gb.mmu.io.sound.output = output
        gb.mmu.io.ppu.lcd.output = this
        gbThread = Thread(gameboy)
        gbThread.start()
    }

    protected fun stopgb() {
        gameboy?.stop()
        gbThread.join()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun create() {
        Gdx.graphics.isContinuousRendering = false

        fpsCounter.enabled = true
        screenTexture = Texture(width, height, Pixmap.Format.RGB888)
        batch = SpriteBatch()
        output.initialize()

        cam.position.set(width.toFloat() / 2f, height.toFloat() / 2f, 0f)
    }

    override fun render(screenBuffer: ByteArray) {
        buffer.put(screenBuffer)
        buffer.rewind()
        Gdx.graphics.requestRendering()
        fpsCounter.frameRendered()
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        Gdx.gl.glTexSubImage2D(screenTexture.glTarget, 0, 0, 0, width, height, GL20.GL_RGB, GL20.GL_UNSIGNED_BYTE, buffer)

        batch.projectionMatrix = cam.combined
        batch.begin()
        batch.draw(screenTexture, 0f, 0f, width.toFloat(), height.toFloat())
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        output.dispose()
        screenTexture.dispose()

        stopgb()
    }
}