package name.antonsmirnov.notes.presenter.addnote

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import name.antonsmirnov.notes.presenter.coroutines.getDefaultDispatcher

/**
 * AddNotePresenter implementation
 */
class AddNotePresenterImpl(
    val model: AddNoteModel,
    val dispatcher: CoroutineDispatcher = getDefaultDispatcher()
) : AddNotePresenter {

    private var view: AddNoteView? = null

    init {
        model.presenter = this
    }

    override suspend fun addNote() {
        model.addNote()
    }

    override fun attachView(view: AddNoteView) {
        if (this.view != null) {
            deinitView()
        }

        initView(view)
        updateView()
    }

    private fun initView(view: AddNoteView) {
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
        GlobalScope.launch(dispatcher) {
            view?.updateModel(model)
            addNote()
            if (model.state !is AddNoteModel.State.ExecutionError) {
                view?.showNotesList()
            }
        }
    }

    override fun onViewDetached() {
        deinitView()
    }
}