package gui

import GameboyDesktop
import Logging
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import nl.stanroelofs.gameboy.GameBoy
import nl.stanroelofs.gameboy.GameBoyCGB
import nl.stanroelofs.gameboy.GameBoyDMG
import nl.stanroelofs.gameboy.memory.cartridge.Cartridge
import java.awt.BorderLayout
import java.awt.Dimension
import java.io.File
import javax.swing.*

class GameboyFrame : JFrame() {

    private var gb: GameBoy? = null
    private val gbapp = GameboyDesktop()
    private val romChooser: RomChooser
    private val optionsDialog = OptionsDialog()
    private val vramViewer = VramViewer()
    private val logger = Logging.getLogger(GameboyFrame::class.java.name)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        romChooser = RomChooser()

        val container = contentPane
        container.layout = BorderLayout()

        val cfg = LwjglApplicationConfiguration()
        cfg.title = "Gameboy"
        cfg.width = GameBoy.SCREEN_WIDTH * 3
        cfg.height = GameBoy.SCREEN_HEIGHT * 3
        val canvas = LwjglAWTCanvas(gbapp, cfg)
        container.add(canvas.canvas, BorderLayout.CENTER)

        val menuBar = JMenuBar()
        val fileMenu = JMenu("File")
        //fileMenu.mnemonic = KeyEvent.VK_ALT
        val loadRom = JMenuItem("Load rom")
        loadRom.addActionListener { loadRom() }
        fileMenu.add(loadRom)
        val options = JMenuItem("Options")
        options.addActionListener { showOptions() }
        fileMenu.add(options)
        val viewer = JMenuItem("Vram Viewer")
        viewer.addActionListener { showVramViewer() }
        fileMenu.add(viewer)
        menuBar.add(fileMenu)

        val gameMenu = JMenu("Game")
        val lockFps = JCheckBoxMenuItem("Lock FPS")
        lockFps.state = true
        lockFps.addActionListener { setLockFps(lockFps.state) }
        gameMenu.add(lockFps)
        val reset = JMenuItem("Reset")
        reset.addActionListener { reset() }
        gameMenu.add(reset)
        val pause = JCheckBoxMenuItem("Pause")
        pause.addActionListener { setPause(pause.state) }
        gameMenu.add(pause)
        val save = JMenuItem("Save")
        save.addActionListener { save() }
        gameMenu.add(save)
        val load = JMenuItem("Load")
        load.addActionListener { load() }
        gameMenu.add(load)
        menuBar.add(gameMenu)

        jMenuBar = menuBar

        container.minimumSize = Dimension(GameBoy.SCREEN_WIDTH, GameBoy.SCREEN_HEIGHT)
        container.preferredSize = Dimension(cfg.width, cfg.height)
        pack()
        isVisible = true
    }

    private fun loadRom() {
        val romFile = romChooser.chooseRom(this) ?: return
        val cartridge = Cartridge(romFile)
        if (cartridge.type == null)
            return

        gb = if (cartridge.isGbc) GameBoyCGB(cartridge) else GameBoyDMG(cartridge)
        optionsDialog.gb = gb
        vramViewer.gb = gb

        if (cartridge.type!!.hasBattery())
            load()

        gbapp.startgb(gb!!)
    }

    private fun showOptions() {
        optionsDialog.isVisible = true
    }

    private fun showVramViewer() {
        vramViewer.isVisible = true
    }

    private fun setPause(state: Boolean) {
        gbapp.setPause(state)
    }

    private fun reset() {
        gb?.reset()
    }

    private fun save() {
        if (gb == null)
            return

        val fileName = gb!!.mmu.cartridge.cartridgeFile.nameWithoutExtension
        try {
            gb!!.mmu.cartridge.saveRam(File("$fileName.sav"))
        } catch (e: Exception) {
            when (e) {
                is IllegalStateException -> logger.e("Failed to save RAM: $e")
                else -> throw e
            }
        }
    }

    private fun load() {
        if (gb == null)
            return

        val fileName = gb!!.mmu.cartridge.cartridgeFile.nameWithoutExtension
        val file = File("$fileName.sav")
        if (!file.exists())
            return

        try {
            gb!!.mmu.cartridge.loadRam(File("$fileName.sav"))
        } catch (e: Exception) {
            when (e) {
                is IllegalStateException, is IllegalArgumentException -> logger.e("Failed to load RAM: $e")
                else -> throw e
            }
        }
    }

    private fun setLockFps(state: Boolean) {
        gbapp.lockFps(state)
    }
}