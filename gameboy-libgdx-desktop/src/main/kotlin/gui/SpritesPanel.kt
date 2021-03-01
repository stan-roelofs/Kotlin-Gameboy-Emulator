package gui

import nl.stanroelofs.gameboy.GameBoy
import nl.stanroelofs.gameboy.memory.io.graphics.PpuCGB
import nl.stanroelofs.gameboy.memory.io.graphics.VSyncListener
import nl.stanroelofs.gameboy.utils.getBit
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.GridLayout
import java.awt.image.BufferedImage
import javax.swing.JPanel

class SpritesPanel : JPanel(), VSyncListener {

    var gb: GameBoy? = null
        set(value) {
            field = value
            gb?.mmu?.io?.ppu?.lcd?.addListener(this)
            for (panel in spritePanels) {
                panel.gb = gb
            }
        }
    private val numSprites = 40
    private val spritePanels = Array(numSprites) { i -> SpritePanel(i) }
    private val spritesPerLine = 8
    private val numberOfLines = numSprites / spritesPerLine
    private val margin = 5
    private var painted = false

    init {
        layout = GridLayout(numberOfLines, spritesPerLine)
        for (panel in spritePanels)
            add(panel)

        minimumSize = Dimension(spritesPerLine * spritePanels[0].minimumSize.width + spritesPerLine * margin, numberOfLines * spritePanels[0].minimumSize.height + numberOfLines * margin)
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        if (g == null)
            return

        for (panel in spritePanels)
            panel.paintComponents(g)

        painted = true
    }

    override fun vsync(screenBuffer: ByteArray) {
        if (!isVisible)
            return

        if (!painted)
            return

        if (gb == null)
            return

        for (sprite in spritePanels)
            sprite.updateImage()

        repaint()
    }
}

class SpritePanel(private val index: Int) : JPanel() {

    var gb: GameBoy? = null

    private val spriteWidth = 8
    private val spriteHeight = 8
    private var scale = 10
    private val image = BufferedImage(spriteWidth * scale, spriteHeight * scale, BufferedImage.TYPE_INT_RGB)

    init {
        minimumSize = Dimension(spriteWidth * scale, spriteHeight * scale)
    }

    fun updateImage() {
        val spritePatternTable = 0x8000
        val spriteAttributeTable = 0xFE00
        val patternNumber = gb!!.mmu.readByte(spriteAttributeTable + (index * 4) + 2)
        val flags = gb!!.mmu.readByte(spriteAttributeTable + (index * 4) + 3)

        for (y in 0 until 8) {
            for (x in 0 until 8) {
                val offSet = patternNumber * 16 + y * 2
                val tile1 = gb!!.mmu.io.ppu.vram.readByte(0, spritePatternTable + offSet)
                val tile2 = gb!!.mmu.io.ppu.vram.readByte(0, spritePatternTable + offSet + 1)

                val bit1 = (tile1 shr (7 - x)) and 0b1
                val bit2 = (tile2 shr (7 - x)) and 0b1
                val colorValue = bit1 or (bit2 shl 1)

                val colorRgb = if (colorValue == 0) {
                    nl.stanroelofs.gameboy.memory.io.graphics.Color(255, 255, 255)
                } else if (gb!!.isGbc) {
                    val palette = flags and 0b111
                    (gb!!.mmu.io.ppu as PpuCGB).objPalettes[palette].getColor(colorValue)
                } else {
                   if (flags.getBit(4)) {
                        gb!!.mmu.io.ppu.obp0.getColor(colorValue)
                    } else {
                        gb!!.mmu.io.ppu.obp1.getColor(colorValue)
                    }
                }
                val color = Color(colorRgb.red, colorRgb.green, colorRgb.blue)

                for (sx in 0 until scale) {
                    for (sy in 0 until scale) {
                        image.setRGB(x * scale + sx, y * scale + sy, color.rgb)
                    }
                }
            }
        }
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        if (g == null)
            return

        g.drawImage(image, 0, 0, null)
    }
}