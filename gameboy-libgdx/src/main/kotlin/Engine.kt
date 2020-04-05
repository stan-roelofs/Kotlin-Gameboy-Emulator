import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import memory.io.sound.SoundOutput
import java.io.File
import java.util.*

class Engine : ApplicationAdapter(), Observer {
    override fun update(o: Observable?, arg: Any?) {

    }

    val color0 = Color(224f / 255, 248f / 255, 208f / 255, 1.0f)
    val color1 = Color(136f / 255, 192f / 255, 112f / 255, 1.0f)
    val color2 = Color(52f / 255, 104f / 255, 86f / 255, 1f)
    val color3 = Color(8f / 255, 24f / 255, 32f / 255, 1f)
    val colors = arrayOf(color0, color1, color2, color3)

    private var batch : SpriteBatch? = null
    private val gb = GameBoy(null)
    private val cart = File("E://Downloads/Legend of Zelda, The - Link's Awakening (U) (V1.2) [!].gb")

    override fun create() {
        batch = SpriteBatch()
        gb.mmu.io.lcd.addObserver(this)
        gb.mmu.io.sound.output = SoundOutput()
        gb.loadCartridge(cart)
        val gbThread = Thread(gb)
        gbThread?.start()
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

        batch?.begin()
        batch?.draw(img, 0f, 0f, 320f, 288f)
        batch?.end()
    }

    override fun dispose() {
        batch?.dispose()
    }
}

fun main(args: Array<String>) {
    var cfg = LwjglApplicationConfiguration()
    cfg.title = "Gameboy"
    cfg.width = 320
    cfg.height = 288
    LwjglApplication(Engine(), cfg)
}