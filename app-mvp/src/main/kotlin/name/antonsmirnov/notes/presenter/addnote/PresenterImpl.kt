package name.antonsmirnov.notes.presenter.addnote

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

    override fun addNote() {
        model.addNote()
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
        // Model is changed so we have to update View
        updateView()
    }

    // as View observer

    override fun onViewChanged() {
        // in Supervising presenter View modifies Model directly
        view?.updateModel(model)
        threadManager.run {
            addNote()
            view?.showNotesList()
        }
    }

    override fun onViewDetached() {
        deinitView()
    }
}