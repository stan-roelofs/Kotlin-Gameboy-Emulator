import gui.GameboyFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager

fun main(args: Array<String>) {
    SwingUtilities.invokeLater {
        GameboyFrame()
    }
}