package gui

import GameboyDesktop
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import gameboy.GameBoy
import utils.Log
import java.awt.BorderLayout
import java.awt.Dimension
import java.io.File
import javax.swing.*


class GameboyFrame : JFrame() {

    private val gb = GameBoy(null)
    private val gbapp = GameboyDesktop(gb)
    private val romChooser: RomChooser

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        romChooser = RomChooser()

        val container = contentPane
        container.layout = BorderLayout()

        val cfg = LwjglApplicationConfiguration()
        cfg.title = "Gameboy"
        cfg.width = 320
        cfg.height = 288
        val canvas = LwjglAWTCanvas(gbapp, cfg)
        container.add(canvas.canvas, BorderLayout.CENTER)

        val menuBar = JMenuBar()
        val fileMenu = JMenu("File")
        //fileMenu.mnemonic = KeyEvent.VK_ALT
        val loadRom = JMenuItem("Load rom")
        loadRom.addActionListener {loadRom() }
        fileMenu.add(loadRom)
        menuBar.add(fileMenu)

        val gameMenu = JMenu("Game")
        val reset = JMenuItem("Reset")
        reset.addActionListener { reset() }
        gameMenu.add(reset)
        val pause = JMenuItem("Toggle pause")
        pause.addActionListener { togglePause() }
        gameMenu.add(pause)
        val save = JMenuItem("Save")
        save.addActionListener { save() }
        gameMenu.add(save)
        val load = JMenuItem("Load")
        load.addActionListener { load() }
        gameMenu.add(load)
        menuBar.add(gameMenu)

        val debugMenu = JMenu("Debug")
        val screenHash = JMenuItem("Screen hash")
        screenHash.addActionListener {screenHash() }
        debugMenu.add(screenHash)
        menuBar.add(debugMenu)

        jMenuBar = menuBar

        container.preferredSize = Dimension(cfg.width, cfg.height)
        pack()
        isVisible = true
    }

    fun loadRom() {
        val rom = romChooser.chooseRom(this)
        if (rom != null) {
            gbapp.stopgb()
            gb.loadCartridge(rom)
            gbapp.startgb()
        }
    }

    fun togglePause() {
        gb.togglePause()
    }

    fun reset() {
        gb.reset()
    }

    fun save() {
        val fileName = gb.cartridge?.cartridgeFile?.nameWithoutExtension
        if (fileName != null)
            gb.cartridge?.saveRam(File("$fileName.sav"))
    }

    fun load() {
        val fileName = gb.cartridge?.cartridgeFile?.nameWithoutExtension
        if (fileName != null)
            gb.cartridge?.loadRam(File("$fileName.sav"))
    }

    fun saveState() {
        TODO("Not yet implemented")
    }

    fun loadState() {
        TODO("Not yet implemented")
    }

    fun screenHash() {
        var s = ""
        for (i in gb.mmu.io.lcd.screenBuffer) {
            for (j in i) {
                s += j.toString()
            }
        }

        Log.d(s.hashCode().toString())
    }
}