package memory.io.graphics

internal enum class Mode(val mode: Int, val cycles: Int) {
    HBLANK(0, 204),
    VBLANK(1, 456),
    OAM_SEARCH(2, 80),
    LCD_TRANSFER(3, 172),
}