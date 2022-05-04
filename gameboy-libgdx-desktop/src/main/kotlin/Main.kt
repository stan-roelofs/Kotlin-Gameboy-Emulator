import gui.GameboyFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager

fun main() {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())

    SwingUtilities.invokeLater {
        GameboyFrame()
    }
}