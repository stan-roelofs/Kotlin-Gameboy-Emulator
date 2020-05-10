import com.badlogic.gdx.Input
import gameboy.GameBoy
import memory.io.Joypad
import memory.io.sound.SoundOutput

class GameboyDesktop(gb: GameBoy) : GameboyLibgdx(gb) {

    override var output: SoundOutput = SoundOutputGdx()

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

    override fun dispose() {
        super.dispose()
        output.dispose()
    }
}