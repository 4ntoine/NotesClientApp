package name.antonsmirnov.notes.presenter.listnotes

import name.antonsmirnov.notes.domain.Note
import name.antonsmirnov.notes.presenter.thread.ThreadManager

/**
 * Presenter implementation
 */
class PresenterImpl(
    val model: Model,
    private val threadManager: ThreadManager
) : Presenter {

    private var view: View? = null

    init {
        model.presenter = this
    }

    override fun start() {
        if (model.state == Model.State.Initial) {
            listNotes()
        }
    }

    override fun listNotes() {
        threadManager.run {
            model.listNotes()
        }
    }

    override fun attachView(view: View) {
        if (this.view != null) {
            deinitView()
        }

        initView(view)
        updateView()
    }

    private fun initView(view: View) {
        this.view = view
        this.view?.presenter = this
    }

    private fun deinitView() {
        view?.presenter = null
        view = null
    }

    private fun updateView() {
        view?.updateView(model)
    }

    // as Model observer

    override fun onModelChanged() {
        updateView()
    }

    // as View observer

    override fun onViewChanged() {
        // nothing
    }

    override fun onLoadRequest() {
        listNotes()
    }

    override fun onViewDetached() {
        deinitView()
    }
}