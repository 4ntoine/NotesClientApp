package name.antonsmirnov.notes.presenter.listnotes

import name.antonsmirnov.notes.presenter.BasePresenter
import name.antonsmirnov.notes.presenter.BaseView
import name.antonsmirnov.notes.presenter.Note

/**
 * MVP : Presenter (Supervising flavor)
 */
interface Presenter : BasePresenter<View> {
    fun start()
    fun listNotes()

    fun onLoadRequest()
}

/**
 * MVP : View (Supervising flavor)
 */
interface View : BaseView<Presenter> {
    fun updateView(model: Model)
}