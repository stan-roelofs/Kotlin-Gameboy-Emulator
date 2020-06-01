package gui

import GameboyDesktop
import GameboyMenu
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import gameboy.GameBoy
import utils.Log
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.KeyEvent
import java.io.File
import javax.swing.*


class GameboyFrame : JFrame(), GameboyMenu {

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
                loadRom(rom)
            }
        }

        val screenHash = JMenuItem("Screen hash")
        screenHash.addActionListener {
            var s = ""
            for (i in gb.mmu.io.lcd.screenBuffer) {
                for (j in i) {
                    s += j.toString()
                }
            }

            Log.d(s.hashCode().toString())
        }

        menu.add(screenHash)

        menu.add(load)
        menuBar.add(menu)

        jMenuBar = menuBar

        container.preferredSize = Dimension(cfg.width, cfg.height)
        pack()
        isVisible = true
    }

    override fun loadRom(file: File) {
        gbapp.stopgb()
        gb.loadCartridge(file)
        gbapp.startgb()
    }

    override fun togglePause() {
        gb.togglePause()
    }

    override fun reset() {
        gb.reset()
    }

    override fun save() {
        TODO("Not yet implemented")
    }

    override fun load() {
        TODO("Not yet implemented")
    }

    override fun saveState() {
        TODO("Not yet implemented")
    }

    override fun loadState() {
        TODO("Not yet implemented")
    }
}