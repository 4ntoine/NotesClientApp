package name.antonsmirnov.notes.presenter.listnotes

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import name.antonsmirnov.notes.presenter.coroutines.getDefaultDispatcher

/**
 * ListNotesPresenter implementation
 */
class ListNotesPresenterImpl(
    val model: ListNotesModel,
    var dispatcher: CoroutineDispatcher = getDefaultDispatcher()
) : ListNotesPresenter {

    private var view: ListNotesView? = null

    init {
        model.presenter = this
    }

    override fun start() {
        if (model.state == ListNotesModel.State.Initial) {
            startListNotes()
        }
    }

    private fun startListNotes() {
        GlobalScope.launch(dispatcher) {
            listNotes()
        }
    }

    override suspend fun listNotes() {
        model.listNotes()
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
        startListNotes()
    }

    override fun onViewDetached() {
        deinitView()
    }
}