package name.antonsmirnov.notes.presenter.serverurl

import name.antonsmirnov.notes.presenter.BasePresenter
import name.antonsmirnov.notes.presenter.BaseView
import kotlin.Exception

/**
 * MVP : Presenter (Passive View flavor)
 */
interface ServerUrlPresenter : BasePresenter<ServerUrlView>

/**
 * MVP : View (Passive View flavor)
 */
interface ServerUrlView : BaseView<ServerUrlPresenter> {
    var host: String
    var port: String

    fun showValidationError(error: Exception)
    fun showNotesList()
}