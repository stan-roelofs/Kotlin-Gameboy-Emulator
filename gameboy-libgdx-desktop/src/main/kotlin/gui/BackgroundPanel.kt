package gui

import gameboy.GameBoy
import gameboy.memory.io.graphics.VSyncListener
import gameboy.utils.getBit
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JPanel

class BackgroundPanel : JPanel(), VSyncListener {

    var gb: GameBoy? = null
        set(value) {
            field = value
            gb?.mmu?.io?.ppu?.lcd?.addListener(this)
        }

    private var scale = 2
    private val numTiles = 32
    private var painted = false
    var bgAddress = 0
    private val image = BufferedImage(numTiles * 8 * scale, numTiles * 8 * scale, BufferedImage.TYPE_INT_RGB)

    init {
        preferredSize = Dimension(image.width, image.height)
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        if (g == null)
            return

        g.drawImage(image, 0, 0, null)
        painted = true
    }

    override fun vsync(screenBuffer: ByteArray) {
        if (!isVisible)
            return

        if (!painted)
            return

        if (gb == null)
            return

        painted = false

        for (i in 0 until numTiles * numTiles) {
            var map = bgAddress
            if (bgAddress == 0) {
                map = if (gb!!.mmu.io.ppu.lcdc.getBackgroundTileMap()) 0x9C00 else 0x9800
            }
            var tileIndex = gb!!.mmu.io.ppu.vram.readByte(0, map + i)
            val addressMode = gb!!.mmu.io.ppu.lcdc.getTileDataSelect()
            if (!addressMode)
                tileIndex = tileIndex.toByte().toInt()

            for (y in 0 until 8) {
                for (x in 0 until 8) {
                    val offSet = tileIndex * 16 + y * 2
                    val bank = if (gb!!.isGbc && gb!!.mmu.io.ppu.vram.readByte(1, map + i).getBit(3)) 1 else 0
                    val tile1 = gb!!.mmu.io.ppu.vram.readByte(bank, if (addressMode) 0x8000 + offSet else 0x9000 + offSet)
                    val tile2 = gb!!.mmu.io.ppu.vram.readByte(bank, if (addressMode) 0x8000 + offSet + 1 else 0x9000 + offSet + 1)

                    val bit1 = (tile1 shr (7 - x)) and 0b1
                    val bit2 = (tile2 shr (7 - x)) and 0b1
                    val colorValue = bit1 or (bit2 shl 1)
                    val colorRgb = gb!!.mmu.io.ppu.bgp.getColor(colorValue)
                    val color = Color(colorRgb.red, colorRgb.green, colorRgb.blue)

                    for (sx in 0 until scale) {
                        for (sy in 0 until scale) {
                            image.setRGB(((i % 32) * 8 + x) * scale + sx, ((i / 32) * 8 + y) * scale + sy, color.rgb)
                        }
                    }
                }
            }
        }

        val scx = gb!!.mmu.io.ppu.scx.value
        val scy = gb!!.mmu.io.ppu.scy.value

        val x1 = scx
        val y1 = scy
        val w1 = Integer.min(GameBoy.SCREEN_WIDTH, GameBoy.SCREENBUFFER_WIDTH - scx)
        val h1 = Integer.min(GameBoy.SCREEN_HEIGHT, GameBoy.SCREENBUFFER_HEIGHT - scy)

        val x2 = 0
        val y2 = 0
        val w2 = (GameBoy.SCREEN_WIDTH - w1)
        val h2 = (GameBoy.SCREEN_HEIGHT - h1)

        val x3 = scx
        val y3 = 0
        val w3 = w1
        val h3 = (GameBoy.SCREEN_HEIGHT - h1)

        val x4 = 0
        val y4 = scy
        val w4 = (GameBoy.SCREEN_WIDTH - w1)
        val h4 = h1

        val g = image.graphics
        g.color = Color.RED
        if (w1 > 0 && h1 > 0)
            g.drawRect(x1 * scale, y1 * scale, w1 * scale, h1 * scale)
        if (w2 > 0 && h2 > 0)
            g.drawRect(x2 * scale, y2 * scale, w2 * scale, h2 * scale)
        if (w3 > 0 && h3 > 0)
            g.drawRect(x3 * scale, y3 * scale, w3 * scale, h3 * scale)
        if (w4 > 0 && h4 > 0)
            g.drawRect(x4 * scale, y4 * scale, w4 * scale, h4 * scale)

        repaint()
    }
}