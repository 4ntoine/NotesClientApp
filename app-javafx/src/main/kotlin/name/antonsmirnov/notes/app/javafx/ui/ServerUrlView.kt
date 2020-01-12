package name.antonsmirnov.notes.app.javafx.ui

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Side
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.Stage
import name.antonsmirnov.notes.app.controller.rest.buildRestApiImpl
import name.antonsmirnov.notes.app.controller.rest.restApiInstance
import name.antonsmirnov.notes.presenter.serverurl.ServerUrlModel
import name.antonsmirnov.notes.presenter.serverurl.ServerUrlPresenter
import name.antonsmirnov.notes.presenter.serverurl.ServerUrlPresenterImpl
import name.antonsmirnov.notes.presenter.serverurl.ServerUrlView

class ServerUrlView(
    val stage: Stage
) : ServerUrlView {

    override var presenter: ServerUrlPresenter? = null

    private lateinit var spVertical: VBox
    private lateinit var tfHost: TextField
    private lateinit var tfPort: TextField
    private lateinit var butOk: Button
    private lateinit var validator: ContextMenu

    private fun show() {
        stage.title = "Server connection"
        initControls()
        setScene(stage)
    }

    private fun initControls() {
        spVertical = VBox()

        tfHost = TextField()
        tfPort = TextField()

        butOk = Button("OK")
        butOk.minWidth = 60.0
        butOk.onAction = EventHandler<ActionEvent> {
            validator.items.clear()
            presenter?.onViewChanged()
        }

        validator = ContextMenu()
        validator.setAutoHide(true)

        spVertical.children.addAll(tfHost, tfPort, butOk)
    }

    private fun setScene(stage: Stage) {
        VBox().apply {
            padding = Insets(5.0)
            stage.scene = Scene(this, 300.0, 100.0)
            children.add(spVertical)
        }
    }

    override var host: String
        get() = tfHost.text
        set(newValue) { tfHost.text = newValue }

    override var port: String
        get() = tfPort.text
        set(newValue) { tfPort.text = newValue }

    override fun showValidationError(error: Exception) {
        validator.apply {
            items.clear()
            items.add(MenuItem(error.message))
            show(butOk, Side.RIGHT, 30.0, 0.0)
        }
    }

    override fun showNotesList() {
        presenter?.onViewDetached()
        ListNotesView.navigate(stage)
    }

    companion object {
        private fun initRestApi(host: String, port: UInt) {
            restApiInstance = buildRestApiImpl("http", host, port)
        }

        fun navigate(stage: Stage) {
            val model = object : ServerUrlModel("localhost", 8080U) {
                override fun update(host: String, port: UInt) {
                    super.update(host, port)
                    initRestApi(host, port)
                }
            }
            val presenter = ServerUrlPresenterImpl(model)
            val view = ServerUrlView(stage)
            view.show()
            presenter.attachView(view)
        }
    }
}