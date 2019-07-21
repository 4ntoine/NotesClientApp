package name.antonsmirnov.notes.app.javafx.ui

import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.ToolBar
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.stage.Stage
import name.antonsmirnov.notes.app.controller.rest.ListNotesController
import name.antonsmirnov.notes.app.controller.rest.RestApi
import name.antonsmirnov.notes.presenter.Note
import name.antonsmirnov.notes.presenter.listnotes.ListNotesModel
import name.antonsmirnov.notes.presenter.listnotes.ListNotesPresenter
import name.antonsmirnov.notes.presenter.listnotes.ListNotesPresenterImpl
import name.antonsmirnov.notes.presenter.listnotes.ListNotesView
import name.antonsmirnov.notes.presenter.thread.BackgroundThreadManager

class ListNotesView(
    val stage: Stage
) : ListNotesView {

    override var presenter: ListNotesPresenter? = null

    private lateinit var tbMain: ToolBar
    private lateinit var butAddNote: Button
    private lateinit var butReload: Button
    private lateinit var piProgress: ProgressIndicator
    private lateinit var spListView: StackPane
    private lateinit var lvNotes: ListView<Note>

    private var notes = FXCollections.observableArrayList<Note>()

    private fun show() {
        stage.title = "Notes"
        initControls()
        setScene()
    }

    private fun initControls() {
        butAddNote = Button("Add")
        butReload = Button("Reload")

        tbMain = ToolBar(butAddNote, butReload)
        butAddNote.minWidth = 60.0
        butAddNote.onAction = EventHandler<ActionEvent> { addNote() }

        butReload.minWidth = 60.0
        butReload.onAction = EventHandler<ActionEvent> { presenter?.onLoadRequest() }

        spListView = StackPane()
        lvNotes = ListView()
        lvNotes.isEditable = false
        lvNotes.setItems(notes)
        lvNotes.setOrientation(Orientation.VERTICAL)
        lvNotes.setCellFactory { NoteListCell() }

        piProgress = ProgressIndicator(ProgressIndicator.INDETERMINATE_PROGRESS)
        val size = 25.0
        piProgress.maxWidth = size
        piProgress.maxHeight = size
        StackPane.setAlignment(piProgress, Pos.CENTER)

        spListView.children.addAll(lvNotes, piProgress)
    }

    class NoteListCell : ListCell<Note>() {
        private val labTitle = Label()
        private val labBody = Label()
        private val vb = VBox(labTitle, labBody)

        init {
            labTitle.font = Font(16.0)
            labBody.font = Font(12.0)
        }

        override fun updateItem(item: Note?, empty: Boolean) {
            super.updateItem(item, empty)
            if (empty) {
                clearItem()
            } else {
                showItem(item)
            }
        }

        private fun clearItem() {
            text = null
            graphic = null
        }

        private fun showItem(item: Note?) {
            labTitle.text = item?.title ?: ""
            labBody.text = item?.body ?: ""

            text = null
            graphic = vb
        }
    }

    private fun addNote() {
        presenter?.onViewDetached()
        AddNoteView.navigate(stage)
    }

    private fun setScene() {
        VBox().apply {
            padding = Insets(5.0)
            stage.scene = Scene(this, 500.0, 300.0)
            children.addAll(tbMain, spListView)
        }
    }

    private fun setIndicatorVisibility(visible: Boolean) {
        piProgress.isVisible = visible
    }

    override fun updateView(_model: ListNotesModel) {
        val model = _model.stateCopy()
        Platform.runLater {
            when (model.state) {
                is ListNotesModel.State.Initial -> {
                    notes.clear()
                    setIndicatorVisibility(false)
                }
                is ListNotesModel.State.Loading -> {
                    notes.clear()
                    setIndicatorVisibility(true)
                }
                is ListNotesModel.State.Loaded -> {
                    notes.clear()
                    notes.addAll((model.state as ListNotesModel.State.Loaded).notes)
                    setIndicatorVisibility(false)
                }
                is ListNotesModel.State.LoadError -> {
                    notes.clear()
                    setIndicatorVisibility(false)
                    showError((model.state as ListNotesModel.State.LoadError).error.message)
                }
            }
        }
    }

    companion object {
        fun navigate(stage: Stage) {
            val model = ListNotesModel(ListNotesController(RestApi.instance))
            val presenter = ListNotesPresenterImpl(model, BackgroundThreadManager())
            val view = ListNotesView(stage)
            view.show()

            presenter.attachView(view)
            presenter.start()
        }
    }
}