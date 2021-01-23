package gameboy.memory.io.graphics.mode

import gameboy.memory.Mmu
import gameboy.memory.io.graphics.SpritePosition
import gameboy.utils.getBit


class OamSearch(private val mmu: Mmu) : Mode {

    enum class State {
        READ_Y,
        READ_X
    }

    val sprites = Array<SpritePosition?>(10) { null }

    var currentSpriteNumber = 0
    var currentSpriteIndex = 0
    var spriteY = 0
    var spriteX = 0
    var state = State.READ_Y

    fun start() {
        currentSpriteNumber = 0
        currentSpriteIndex = 0
        spriteY = 0
        spriteX = 0
        sprites.fill(null)
        state = State.READ_Y
    }

    override fun tick() {
        val spriteAddress = 0xFE00 + 4 * currentSpriteNumber

        when(state) {
            State.READ_Y -> {
                spriteY = mmu.readByte(spriteAddress)
                state = State.READ_X
            }
            State.READ_X -> {
                spriteX = mmu.readByte(spriteAddress + 1)
                val spriteSize = if (mmu.readByte(Mmu.LCDC).getBit(2)) 16 else 8
                if (currentSpriteIndex < sprites.size && (mmu.readByte(Mmu.LY) + 16) in spriteY until spriteY + spriteSize) {
                    sprites[currentSpriteIndex++] = SpritePosition(spriteX, spriteY, spriteAddress)
                }
                state = State.READ_Y
                ++currentSpriteNumber
            }
        }
    }

    override fun finished(): Boolean {
        return currentSpriteNumber == 40
    }
}