package name.antonsmirnov.notes.app.javafx.ui

import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.control.ProgressIndicator
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import name.antonsmirnov.notes.app.controller.rest.AddNoteController
import name.antonsmirnov.notes.app.controller.rest.restApiInstance
import name.antonsmirnov.notes.presenter.Note
import name.antonsmirnov.notes.presenter.addnote.AddNoteModel
import name.antonsmirnov.notes.presenter.addnote.AddNotePresenter
import name.antonsmirnov.notes.presenter.addnote.AddNotePresenterImpl
import name.antonsmirnov.notes.presenter.addnote.AddNoteView

class AddNoteView(
    val stage: Stage
) : AddNoteView {

    override var presenter: AddNotePresenter? = null

    private lateinit var tfTitle: TextField
    private lateinit var spBody: StackPane
    private lateinit var taBody: TextArea
    private lateinit var piProgress: ProgressIndicator
    private lateinit var hBox: HBox
    private lateinit var butAdd: Button
    private lateinit var butCancel: Button

    private fun show() {
        stage.title = "Add note"
        initControls()
        setScene()
    }

    private fun initControls() {
        tfTitle = TextField()
        taBody = TextArea()

        piProgress = ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS)
        val size = 25.0
        piProgress.maxWidth = size
        piProgress.maxHeight = size
        StackPane.setAlignment(piProgress, Pos.CENTER)

        spBody = StackPane(taBody, piProgress)
        spBody.minHeight = 235.0

        butAdd = Button("Add note")
        butAdd.minWidth = 60.0
        butAdd.onAction = EventHandler { presenter?.onViewChanged() }

        butCancel = Button("Cancel")
        butCancel.minWidth = 60.0
        butCancel.onAction = EventHandler {
            presenter?.onViewDetached()
            ListNotesView.navigate(stage)
        }

        hBox = HBox(butAdd, butCancel)
    }

    private fun setScene() {
        VBox().apply {
            padding = Insets(5.0)
            stage.scene = Scene(this, 500.0, 300.0)
            children.addAll(tfTitle, spBody, hBox)
        }
    }

    private fun setIndicatorVisibility(visible: Boolean) {
        piProgress.isVisible = visible
    }

    override fun updateView(_model: AddNoteModel) {
        val model = _model.stateCopy()
        Platform.runLater {
            when (model.state) {
                is AddNoteModel.State.Initial -> {
                    setIndicatorVisibility(false)
                }
                is AddNoteModel.State.Executing -> {
                    setIndicatorVisibility(true)
                }
                is AddNoteModel.State.ExecutionError -> {
                    setIndicatorVisibility(false)
                    showError((model.state as AddNoteModel.State.ExecutionError).error.message)
                }
                is AddNoteModel.State.Executed -> {
                    setIndicatorVisibility(false)
                }
            }
        }
    }

    override fun updateModel(model: AddNoteModel) {
        model.note.title = tfTitle.text
        model.note.body = if (taBody.text.isNotEmpty()) taBody.text else null
    }

    override fun showNotesList() = Platform.runLater {
        presenter?.onViewDetached()
        ListNotesView.navigate(stage)
    }

    companion object {
        fun navigate(stage: Stage) {
            val model = AddNoteModel(AddNoteController(restApiInstance), Note("", null))
            val presenter = AddNotePresenterImpl(model)
            val view = AddNoteView(stage)
            view.show()

            presenter.attachView(view)
        }
    }
}