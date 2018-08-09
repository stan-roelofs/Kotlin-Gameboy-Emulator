import cpu.Registers
import memory.Mmu

class Gpu(private val registers: Registers) {
    private var mode = 0
    private var modeClock = 0
    private var line = 0
    private var lastClock = 0
    var screen = Array(144, {IntArray(160)})
    private val mmu = Mmu.instance
    var frameDone = false

    fun reset() {
        mode = 0
        modeClock = 0
        line = 0
        lastClock = 0
    }

    fun step() {
        val newClock = registers.clock
        val dif = newClock - lastClock
        lastClock = newClock

        modeClock += dif

        when(mode) {
            // OAM read mode
            2 -> {
                if (modeClock >= 80) {
                    // Enter scanline mode 3
                    modeClock = 0
                    mode = 3
                }
            }

            // VRAM read mode
            3 -> {
                if (modeClock >= 172) {
                    // Enter hblank
                    modeClock = 0
                    mode = 0

                    renderScan()
                }
            }

            // Hblank
            0 -> {
                if (modeClock >= 204) {
                    modeClock = 0
                    line++

                    if (line == 143) {
                        // enter vblank
                        mode = 1
                        frameDone = true
                    } else {
                        mode = 2
                    }
                }
            }

            // Vblank
            1 -> {
                if (modeClock >= 456) {
                    modeClock = 0
                    line++

                    if (line > 153) {
                        mode = 2
                        line = 0
                    }
                }
            }
        }
    }

    fun renderScan() {
        // LY
        val ly = mmu.readByte(0xFF44)
        //val ly = line
        val scy = mmu.readByte(0xFF42)

        val yPos = (ly + scy) % 256

        for (x in 0 until 160) {
            val scx = mmu.readByte(0xFF43)
            val xPos = (scx + x) % 256

            val row = (yPos shr 3) and 0xFF
            val col = (xPos shr 3) and 0xFF

            val BGTileMap = if ((mmu.readByte(0xFF40) and 0x8) == 0x0) 0x9800 else 0x9C00
            var BGTileNum = (BGTileMap + (row * 32) + col) and 0xFF

            var tileAddr = 0x8000

            if ((mmu.readByte(0xFF40) and 0x10) == 0x0) {
                tileAddr = 0x9000
                if (BGTileNum > 127) {
                    BGTileNum -= 256
                }
            }

            tileAddr += (BGTileNum * 16)

            val line = (yPos % 8) / 2

            val data1 = mmu.readByte(tileAddr + line)
            val data2 = mmu.readByte(tileAddr + line + 1)

            val colorBit = 7 - (xPos % 8)
            var colorNum = if ((data2 and (1 shl colorBit)) == 0) 0 else 0x2
            val colorOr = if ((colorNum or (data1 and (1 shl colorBit))) == 0) 0 else 0x1
            colorNum = colorNum or colorOr

            val bgp = mmu.readByte(0xFF47)
            val color = ((bgp shr (2 * colorNum)) and 0x03)

            screen[ly][x] = color
        }
    }
}