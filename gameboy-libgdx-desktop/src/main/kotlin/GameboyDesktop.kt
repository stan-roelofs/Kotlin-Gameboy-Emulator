import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import gameboy.memory.io.Joypad
import gameboy.memory.io.sound.SoundOutput

class GameboyDesktop : GameboyLibgdx() {

    override var output: SoundOutput = SoundOutputGdx()

    override fun create() {
        super.create()
        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun keyUp(keycode: Int): Boolean {
                when (keycode) {
                    Input.Keys.ENTER -> gameboy.mmu.io.joypad.keyReleased(Joypad.JoypadKey.START)
                    Input.Keys.LEFT -> gameboy.mmu.io.joypad.keyReleased(Joypad.JoypadKey.LEFT)
                    Input.Keys.RIGHT -> gameboy.mmu.io.joypad.keyReleased(Joypad.JoypadKey.RIGHT)
                    Input.Keys.UP -> gameboy.mmu.io.joypad.keyReleased(Joypad.JoypadKey.UP)
                    Input.Keys.DOWN -> gameboy.mmu.io.joypad.keyReleased(Joypad.JoypadKey.DOWN)
                    Input.Keys.Z -> gameboy.mmu.io.joypad.keyReleased(Joypad.JoypadKey.A)
                    Input.Keys.X -> gameboy.mmu.io.joypad.keyReleased(Joypad.JoypadKey.B)
                    Input.Keys.TAB -> gameboy.mmu.io.joypad.keyReleased(Joypad.JoypadKey.SELECT)
                    else -> {}
                }
                return true
            }

            override fun keyDown(keycode: Int): Boolean {
                when (keycode) {
                    Input.Keys.ENTER -> gameboy.mmu.io.joypad.keyPressed(Joypad.JoypadKey.START)
                    Input.Keys.LEFT -> gameboy.mmu.io.joypad.keyPressed(Joypad.JoypadKey.LEFT)
                    Input.Keys.RIGHT -> gameboy.mmu.io.joypad.keyPressed(Joypad.JoypadKey.RIGHT)
                    Input.Keys.UP -> gameboy.mmu.io.joypad.keyPressed(Joypad.JoypadKey.UP)
                    Input.Keys.DOWN -> gameboy.mmu.io.joypad.keyPressed(Joypad.JoypadKey.DOWN)
                    Input.Keys.Z -> gameboy.mmu.io.joypad.keyPressed(Joypad.JoypadKey.A)
                    Input.Keys.X -> gameboy.mmu.io.joypad.keyPressed(Joypad.JoypadKey.B)
                    Input.Keys.TAB -> gameboy.mmu.io.joypad.keyPressed(Joypad.JoypadKey.SELECT)
                    else -> {}
                }
                return true
            }
        }
    }

    override fun dispose() {
        super.dispose()
        output.dispose()
    }
}