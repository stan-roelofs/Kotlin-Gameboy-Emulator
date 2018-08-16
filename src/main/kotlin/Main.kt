import gui.GameBoyView
import javafx.stage.Stage
import tornadofx.App

class Main : App(GameBoyView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        //stage.isFullScreen = true
    }
}