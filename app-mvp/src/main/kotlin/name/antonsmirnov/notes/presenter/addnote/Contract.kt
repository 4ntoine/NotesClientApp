package name.antonsmirnov.notes.presenter.addnote

import name.antonsmirnov.notes.presenter.BasePresenter
import name.antonsmirnov.notes.presenter.BaseView

/**
 * MVP : Presenter (Supervising flavor)
 */
interface Presenter : BasePresenter<View> {
    fun addNote()
}

/**
 * MVP : View (Supervising flavor)
 */
interface View : BaseView<Presenter> {
    fun updateView(model: Model)
    fun updateModel(model: Model)
    fun showNotesList()
}