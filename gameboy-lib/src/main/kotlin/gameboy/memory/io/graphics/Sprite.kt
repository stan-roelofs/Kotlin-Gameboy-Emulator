package gameboy.memory.io.graphics

abstract class Sprite {
    var y = 0
    var x = 0
    var tileNumber = 0
    var priority = false
    var isYFlip = false
    var isXFlip = false
}

class SpriteDMG : Sprite() {
    var isPalette1 = false
}

class SpriteCGB : Sprite() {
    var vramBank1 = false
    var paletteNumber = 0
}