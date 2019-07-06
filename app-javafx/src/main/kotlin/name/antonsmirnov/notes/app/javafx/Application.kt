package name.antonsmirnov.notes.app.javafx

import javafx.stage.Stage
import name.antonsmirnov.notes.app.javafx.ui.ServerUrlView

class Application : javafx.application.Application() {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Application::class.java)
        }
    }

    override fun start(stage: Stage) {
        stage.centerOnScreen()
        ServerUrlView.navigate(stage)
        stage.show()
    }
}