package name.antonsmirnov.notes.presenter.addnote

import name.antonsmirnov.notes.presenter.BasePresenter
import name.antonsmirnov.notes.presenter.BaseView

/**
 * MVP : Presenter (Supervising flavor)
 */
interface AddNotePresenter : BasePresenter<AddNoteView> {
    suspend fun addNote()
}

/**
 * MVP : View (Supervising flavor)
 */
interface AddNoteView : BaseView<AddNotePresenter> {
    fun updateView(model: AddNoteModel)
    fun updateModel(model: AddNoteModel)
    fun showNotesList()
}