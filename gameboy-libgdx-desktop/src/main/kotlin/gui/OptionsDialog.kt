package gui

import nl.stanroelofs.gameboy.GameBoy
import java.awt.BorderLayout
import javax.swing.JCheckBox
import javax.swing.JDialog
import javax.swing.JPanel

class OptionsDialog : JDialog() {

    var gb : GameBoy? = null
        set(value) {
            field = value
            onVisible()
        }
    private val sound1: JCheckBox
    private val sound2: JCheckBox
    private val sound3: JCheckBox
    private val sound4: JCheckBox

    init {
        layout = BorderLayout()
        val panel = JPanel()

        sound1 = JCheckBox("Sound channel 1 - Tone & Sweep")
        sound1.addActionListener {
            setSoundChannelEnabled(0, sound1.isSelected)
        }
        sound2 = JCheckBox("Sound channel 2 - Tone")
        sound2.addActionListener {
            setSoundChannelEnabled(1, sound2.isSelected)
        }
        sound3 = JCheckBox("Sound channel 3 - Wave Output")
        sound3.addActionListener {
            setSoundChannelEnabled(2, sound3.isSelected)
        }
        sound4 = JCheckBox("Sound channel 4 - Noise")
        sound4.addActionListener {
            setSoundChannelEnabled(3, sound4.isSelected)
        }
        panel.add(sound1)
        panel.add(sound2)
        panel.add(sound3)
        panel.add(sound4)
        add(panel, BorderLayout.CENTER)
        pack()
    }

    private fun onVisible() {
        sound1.isSelected = gb?.mmu?.io?.sound?.optionChannelEnables?.get(0) ?: false
        sound2.isSelected = gb?.mmu?.io?.sound?.optionChannelEnables?.get(1) ?: false
        sound3.isSelected = gb?.mmu?.io?.sound?.optionChannelEnables?.get(2) ?: false
        sound4.isSelected = gb?.mmu?.io?.sound?.optionChannelEnables?.get(3) ?: false
    }

    override fun setVisible(b: Boolean) {
        if (b)
            onVisible()

        super.setVisible(b)
    }

    private fun setSoundChannelEnabled(channel: Int, enabled: Boolean) {
        if (gb == null)
            return

        if (channel !in 0 until 4)
            throw IllegalArgumentException("Channel $channel does not exist!")

        gb!!.mmu.io.sound.optionChannelEnables[channel] = enabled
    }
}