package gui

import GameboyDesktop
import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import nl.stanroelofs.gameboy.GameBoy
import nl.stanroelofs.gameboy.GameBoyCGB
import nl.stanroelofs.gameboy.GameBoyDMG
import nl.stanroelofs.gameboy.memory.io.sound.Sound
import nl.stanroelofs.gameboy.utils.Buffer
import java.awt.BorderLayout
import java.awt.Dimension
import java.io.File
import javax.swing.*

class GameboyFrame() : JFrame() {

    private var file: File? = null

    private var gb: GameBoy? = null
    private val gbapp = GameboyDesktop()
    private val romChooser: RomChooser
    private val vramViewer = VramViewer()
    //private val logger = Logging.getLogger(GameboyFrame::class.java.name)

    private val enablesound = JCheckBoxMenuItem("Enable sound", true)
    private val sound1 = JCheckBoxMenuItem("Sound channel 1 - Tone & Sweep", true)
    private val sound2 = JCheckBoxMenuItem("Sound channel 2 - Tone", true)
    private val sound3 = JCheckBoxMenuItem("Sound channel 3 - Wave Output", true)
    private val sound4 = JCheckBoxMenuItem("Sound channel 4 - Noise", true)
    private val lockFPS = JCheckBoxMenuItem("Lock FPS", true)

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
        menuBar.add(fileMenu)

        val gameMenu = JMenu("Game")
        lockFPS.addActionListener { updateLockFps() }
        gameMenu.add(lockFPS)
        val reset = JMenuItem("Reset")
        reset.addActionListener { reset() }
        gameMenu.add(reset)
        val pause = JCheckBoxMenuItem("Pause")
        pause.addActionListener { setPause(pause.state) }
        gameMenu.add(pause)
        gameMenu.addSeparator()
        val save = JMenuItem("Save game")
        save.addActionListener { save() }
        gameMenu.add(save)
        val load = JMenuItem("Load save")
        load.addActionListener { load() }
        gameMenu.add(load)
        menuBar.add(gameMenu)

        val debugMenu = JMenu("Debug")

        sound1.addActionListener { updateSoundChannels() }
        sound2.addActionListener { updateSoundChannels() }
        sound3.addActionListener { updateSoundChannels() }
        sound4.addActionListener { updateSoundChannels() }
        enablesound.addActionListener { updateSoundChannels() }

        debugMenu.add(enablesound)
        debugMenu.add(sound1)
        debugMenu.add(sound2)
        debugMenu.add(sound3)
        debugMenu.add(sound4)

        debugMenu.addSeparator()

        val viewer = JMenuItem("VRAM Viewer")
        viewer.addActionListener { showVramViewer() }
        debugMenu.add(viewer)
        menuBar.add(debugMenu)

        jMenuBar = menuBar

        container.minimumSize = Dimension(GameBoy.SCREEN_WIDTH, GameBoy.SCREEN_HEIGHT)
        container.preferredSize = Dimension(cfg.width, cfg.height)
        pack()
        isVisible = true
    }

    private fun loadRom() {
        file = romChooser.chooseRom(this) ?: return

        gb = if (file!!.name.endsWith("gbc")) GameBoyCGB() else GameBoyDMG()
        gb!!.cartridge.load(Buffer(file!!.readBytes().toTypedArray()))
        vramViewer.gb = gb

        if (gb!!.cartridge.type!!.hasBattery)
            load()

        gbapp.startgb(gb!!)
        updateLockFps()
        updateSoundChannels()
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

        val fileName = file!!.nameWithoutExtension
        try {
            val buffer = Buffer<Byte>()
            gb!!.cartridge.saveRam(buffer)
            File("$fileName.sav").writeBytes(buffer.toList().toByteArray())
        } catch (e: Exception) {
            when (e) {
                is IllegalStateException -> 0 //logger.e{"Failed to save RAM: $e"}
                else -> throw e
            }
        }
    }

    private fun load() {
        if (gb == null)
            return

        val fileName = file!!.nameWithoutExtension
        val file = File("$fileName.sav")
        if (!file.exists())
            return

        try {
            val buffer = Buffer(File("$fileName.sav").readBytes().toTypedArray())
            gb!!.mmu.cartridge.loadRam(buffer)
        } catch (e: Exception) {
            when (e) {
                // is IllegalStateException, is IllegalArgumentException -> logger.e{"Failed to load RAM: $e"}
                else -> throw e
            }
        }
    }

    private fun updateLockFps() = gbapp.lockFps(lockFPS.state)

    private fun updateSoundChannels() {
        if (gb == null)
            return

        if (enablesound.state) gb!!.mmu.io.sound.output?.enable() else gb!!.mmu.io.sound.output?.disable()
        gb!!.mmu.io.sound.channels[Sound.Channels.SQUARE1.ordinal].muted = !sound1.state
        gb!!.mmu.io.sound.channels[Sound.Channels.SQUARE2.ordinal].muted = !sound2.state
        gb!!.mmu.io.sound.channels[Sound.Channels.WAVE.ordinal].muted = !sound3.state
        gb!!.mmu.io.sound.channels[Sound.Channels.NOISE.ordinal].muted = !sound4.state
    }
}