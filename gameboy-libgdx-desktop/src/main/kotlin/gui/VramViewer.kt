package gui

import gameboy.GameBoy
import java.awt.BorderLayout
import java.awt.Dimension
import java.lang.Integer.max
import javax.swing.*

class VramViewer : JDialog() {

    var gb: GameBoy? = null
        set(value) {
            field = value
            backgroundPanel.gb = gb
            spritesPanel.gb = gb
        }


    private val tabbedPane = JTabbedPane()
    private val backgroundPanel = BackgroundPanel()
    private val spritesPanel = SpritesPanel()

    init {
        layout = BorderLayout()

        val p = JPanel()
        p.layout = BorderLayout()
        p.add(backgroundPanel, BorderLayout.CENTER)

        val mapPanel = JPanel()
        val group = ButtonGroup()
        val bgMap1 = JRadioButton("Auto")
        bgMap1.isSelected = true
        bgMap1.addActionListener {
            backgroundPanel.bgAddress = 0
        }
        val bgMap2 = JRadioButton("0x9800")
        bgMap2.addActionListener {
            backgroundPanel.bgAddress = 0x9800
        }
        val bgMap3 = JRadioButton("0x9C00")
        bgMap3.addActionListener {
            backgroundPanel.bgAddress = 0x9C00
        }
        group.add(bgMap1)
        group.add(bgMap2)
        group.add(bgMap3)
        mapPanel.add(bgMap1)
        mapPanel.add(bgMap2)
        mapPanel.add(bgMap3)
        p.add(mapPanel, BorderLayout.SOUTH)

        val p2 = JPanel()
        p2.layout = BorderLayout()
        p2.add(spritesPanel, BorderLayout.CENTER)

        add(tabbedPane)
        tabbedPane.addTab("Tiles", p)
        tabbedPane.addTab("Sprites", p2)

        minimumSize = Dimension(max(p.minimumSize.width, p2.minimumSize.width), max(p.minimumSize.height, p2.minimumSize.height))
        pack()
    }
}

