package gui

import GameboyDesktop
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import gameboy.GameBoy
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.KeyEvent
import javax.swing.*


class GameboyFrame : JFrame() {

    private val gb = GameBoy(null)
    private val gbapp = GameboyDesktop(gb)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        val container = contentPane
        container.layout = BorderLayout()

        val cfg = LwjglApplicationConfiguration()
        cfg.title = "Gameboy"
        cfg.width = 320
        cfg.height = 288
        val canvas = LwjglAWTCanvas(gbapp, cfg)
        container.add(canvas.canvas, BorderLayout.CENTER)

        val menuBar = JMenuBar()
        val menu = JMenu("File")
        menu.mnemonic = KeyEvent.VK_ALT
        val load = JMenuItem("Load rom")
        load.addActionListener {
            val rom = RomChooser().chooseRom(this)
            if (rom != null) {
                gbapp.stopgb()
                gb.loadCartridge(rom)
                gbapp.startgb()

                pack()
            }
        }

        menu.add(load)
        menuBar.add(menu)

        jMenuBar = menuBar

        container.preferredSize = Dimension(cfg.width, cfg.height)
        pack()
        isVisible = true
    }
}