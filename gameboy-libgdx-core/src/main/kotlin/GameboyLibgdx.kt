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
import memory.io.sound.SoundOutput
import java.nio.ByteBuffer
import java.util.*

/** Base libgdx gameboy class
 *
 * Contains main rendering logic and platform independent functionality
 */
abstract class GameboyLibgdx(protected val gb: GameBoy) : ApplicationAdapter(), Observer {

    class Color(val r: Byte, val g: Byte, val b: Byte)

    private val color0 = Color(224.toByte(), 248f.toByte(), 208f.toByte())
    private val color1 = Color(136.toByte(), 192.toByte(), 112f.toByte())
    private val color2 = Color(52.toByte(), 104.toByte(), 86.toByte())
    private val color3 = Color(8.toByte(), 24.toByte(), 32.toByte())
    private val colors = arrayOf(color0, color1, color2, color3)

    val width = 160
    val height = 144

    private lateinit var font: BitmapFont
    private lateinit var batch : SpriteBatch
    private lateinit var screenTexture: Texture

    /** Sound output object, required by gameboy */
    abstract var output : SoundOutput
    protected var gbThread = Thread(gb)
    protected val cam = OrthographicCamera()
    protected val viewport = StretchViewport(width.toFloat(), height.toFloat(), cam)
    protected var fpsCounter = FpsCounter()
    private var framesCounter = 0
    private var lastTime = 0L

    private val screenBufferArray = ByteArray(width * height * 3)
    private val buffer = ByteBuffer.allocateDirect(width * height * 3)
    
    open fun startgb() {
        gbThread = Thread(gb)
        gbThread.start()
    }

    fun stopgb() {
        gb.stop()
        gbThread.join()
    }

    override fun update(o: Observable?, arg: Any?) {
        fpsCounter.frameRendered()

        val screenBuffer = arg as Array<IntArray>
        for (i in 0 until height) {
            for (j in 0 until width) {
                val tmp = colors[screenBuffer[i][j]]
                screenBufferArray[i * width * 3 + j * 3] = tmp.r
                screenBufferArray[i * width * 3 + j * 3 + 1] = tmp.g
                screenBufferArray[i * width * 3 + j * 3 + 2] = tmp.b
            }
        }

        buffer.put(screenBufferArray)
        buffer.rewind()
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
        gb.mmu.io.sound.output = output
        gb.mmu.io.lcd.addObserver(this)
        cam.position.set(width.toFloat() / 2f, height.toFloat() / 2f, 0f)
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
        font.dispose()
        screenTexture.dispose()

        gb.stop()
        gbThread.join()
    }
}