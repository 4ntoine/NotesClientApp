package name.antonsmirnov.notes.presenter.listnotes

import name.antonsmirnov.notes.presenter.BasePresenter
import name.antonsmirnov.notes.presenter.BaseView

/**
 * MVP : Presenter (Supervising flavor)
 */
interface ListNotesPresenter : BasePresenter<ListNotesView> {
    fun start()
    fun listNotes()

    fun onLoadRequest()
}

/**
 * MVP : View (Supervising flavor)
 */
interface ListNotesView : BaseView<ListNotesPresenter> {
    fun updateView(model: ListNotesModel)
}