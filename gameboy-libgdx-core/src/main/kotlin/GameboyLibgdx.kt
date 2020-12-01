import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.StretchViewport
import gameboy.GameBoy
import gameboy.memory.io.sound.SoundOutput
import java.nio.ByteBuffer

/** Base libgdx gameboy class
 *
 * Contains main rendering logic and platform independent functionality
 */
abstract class GameboyLibgdx : ApplicationAdapter() {

    class Color(val r: Byte, val g: Byte, val b: Byte)

    val width = GameBoy.SCREEN_WIDTH
    val height = GameBoy.SCREEN_HEIGHT

    private lateinit var font: BitmapFont
    private lateinit var batch : SpriteBatch
    private lateinit var screenTexture: Texture

    /** Sound output object, required by gameboy */
    abstract var output : SoundOutput
    lateinit var gameboy: GameBoy
        private set

    protected lateinit var gbThread: Thread
    protected val cam = OrthographicCamera()
    protected val viewport = StretchViewport(width.toFloat(), height.toFloat(), cam)
    protected var fpsCounter = FpsCounter()

    private val screenBufferArray = ByteArray(width * height * 3)
    private val buffer = ByteBuffer.allocateDirect(width * height * 3)
    
    open fun startgb(gb: GameBoy) {
        if (this::gameboy.isInitialized && gameboy.running)
            stopgb()

        gameboy = gb
        gb.mmu.io.sound.output = output
        gbThread = Thread(gameboy)
        gbThread.start()
    }

    protected fun stopgb() {
        gameboy.stop()
        gbThread.join()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun create() {
        font = BitmapFont()
        font.data.scale(-0.8f)
        screenTexture = Texture(width, height, Pixmap.Format.RGB888)
        batch = SpriteBatch()
        output.initialize()

        cam.position.set(width.toFloat() / 2f, height.toFloat() / 2f, 0f)
    }

    override fun render() {
        if (this::gameboy.isInitialized) {
            val screenBuffer = gameboy.mmu.io.ppu.lcd.lastBuffer
            for (y in 0 until GameBoy.SCREEN_HEIGHT) {
                for (x in 0 until GameBoy.SCREEN_WIDTH) {
                    val red = screenBuffer[y * GameBoy.SCREEN_WIDTH * 3 + x * 3]
                    val green = screenBuffer[y * GameBoy.SCREEN_WIDTH * 3 + x * 3 + 1]
                    val blue = screenBuffer[y * GameBoy.SCREEN_WIDTH * 3 + x * 3 + 2]

                    screenBufferArray[y * GameBoy.SCREEN_WIDTH * 3 + x * 3] = red.toByte()
                    screenBufferArray[y * GameBoy.SCREEN_WIDTH * 3 + x * 3 + 1] = green.toByte()
                    screenBufferArray[y * GameBoy.SCREEN_WIDTH * 3 + x * 3 + 2] = blue.toByte()
                }
            }
        }

        buffer.put(screenBufferArray)
        buffer.rewind()

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
        font.dispose()
        screenTexture.dispose()

        stopgb()
    }
}