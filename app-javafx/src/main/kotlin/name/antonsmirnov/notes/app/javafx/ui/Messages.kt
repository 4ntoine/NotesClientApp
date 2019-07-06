package name.antonsmirnov.notes.app.javafx.ui

import javafx.scene.control.Alert

fun showError(message: String?) {
    Alert(Alert.AlertType.ERROR).apply {
        headerText = "Load error"
        contentText = message
        showAndWait()
    }
}