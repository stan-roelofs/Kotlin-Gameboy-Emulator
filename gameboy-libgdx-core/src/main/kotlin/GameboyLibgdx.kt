import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.StretchViewport
import gameboy.GameBoy
import memory.io.sound.SoundOutput
import java.util.*

/** Base libgdx gameboy class
 *
 * Contains main rendering logic and platform independent functionality
 */
abstract class GameboyLibgdx(protected val gb: GameBoy) : ApplicationAdapter(), Observer {

    private val color0 = Color(224f / 255, 248f / 255, 208f / 255, 1.0f)
    private val color1 = Color(136f / 255, 192f / 255, 112f / 255, 1.0f)
    private val color2 = Color(52f / 255, 104f / 255, 86f / 255, 1f)
    private val color3 = Color(8f / 255, 24f / 255, 32f / 255, 1f)
    private val colors = arrayOf(color0, color1, color2, color3)

    val width = 160
    val height = 144

    protected var batch : SpriteBatch? = null

    /** Sound output object, required by gameboy */
    abstract var output : SoundOutput
    private var gbThread = Thread(gb)
    protected val cam = OrthographicCamera()
    protected val viewport = StretchViewport(width.toFloat(), height.toFloat(), cam)
    protected var fpsCounter = FpsCounter()
    private var framesCounter = 0
    private var lastTime = 0L

    private var lastFrame = Array(144) {IntArray(160)}

    fun startgb() {
        gbThread = Thread(gb)
        gbThread.start()
    }

    fun stopgb() {
        gb.stop()
        gbThread.join()
    }

    override fun update(o: Observable?, arg: Any?) {
        fpsCounter.frameRendered()
        lastFrame = arg as Array<IntArray>
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun create() {
        batch = SpriteBatch()
        output.initialize()
        gb.mmu.io.sound.output = output
        gb.mmu.io.lcd.addObserver(this)
        cam.position.set(width.toFloat() / 2f, height.toFloat() / 2f, 0f)
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val pixmap = Pixmap(width, height, Pixmap.Format.RGB888)
        for (y in 0 until height) {
            for (x in 0 until width) {
                pixmap.setColor(colors[lastFrame[y][x]])
                pixmap.drawPixel(x, y)
            }
        }

        val img = Texture(pixmap)

        batch?.projectionMatrix = cam.combined
        batch?.begin()
        batch?.draw(img, 0f, 0f, width.toFloat(), height.toFloat())
        batch?.end()
    }

    override fun dispose() {
        batch?.dispose()
        output.dispose()
        gb.stop()
        gbThread.join()
    }
}