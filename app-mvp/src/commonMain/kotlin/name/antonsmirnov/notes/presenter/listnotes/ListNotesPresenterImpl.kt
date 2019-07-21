package name.antonsmirnov.notes.presenter.listnotes

import name.antonsmirnov.notes.presenter.thread.ThreadManager

/**
 * ListNotesPresenter implementation
 */
class ListNotesPresenterImpl(
    val model: ListNotesModel,
    private val threadManager: ThreadManager
) : ListNotesPresenter {

    private var view: ListNotesView? = null

    init {
        model.presenter = this
    }

    override fun start() {
        if (model.state == ListNotesModel.State.Initial) {
            listNotes()
        }
    }

    override fun listNotes() {
        threadManager.run {
            model.listNotes()
        }
    }

    override fun attachView(view: ListNotesView) {
        if (this.view != null) {
            deinitView()
        }

        initView(view)
        updateView()
    }

    private fun initView(view: ListNotesView) {
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