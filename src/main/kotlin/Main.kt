import gui.GameBoyView
import javafx.application.Application
import javafx.stage.Stage
import tornadofx.App

class Main : App(GameBoyView::class) {
    override fun start(stage: Stage) {
        stage.title = "KGB"

        super.start(stage)
        //stage.isFullScreen = true
    }
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}