package name.antonsmirnov.notes.presenter.serverurl

import name.antonsmirnov.notes.presenter.BasePresenter
import name.antonsmirnov.notes.presenter.BaseView
import java.lang.Exception

/**
 * MVP : Presenter (Passive View flavor)
 */
interface Presenter : BasePresenter<View>

/**
 * MVP : View (Passive View flavor)
 */
interface View : BaseView<Presenter> {
    var host: String
    var port: String

    fun showValidationError(error: Exception)
    fun showNotesList()
}