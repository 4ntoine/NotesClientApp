package name.antonsmirnov.notes.presenter.serverurl

class PresenterImpl(val model: Model) : Presenter {

    private var view: View? = null

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
        view?.let {
            it.host = model.host ?: ""
            it.port = model.port?.toString() ?: ""
        }
    }

    // as Model observer
    override fun onModelChanged() {
        view?.let { updateView() }
    }

    // as View observer

    override fun onViewChanged() {
        view?.let {
            if (!isValidView(it)) {
                return
            }
            model.update(it.host, it.port.toUInt())
            view?.showNotesList()
        }
    }

    private fun isValidView(view: View): Boolean {
        try {
            if (view.host.isEmpty())
                throw Exception("Host can not be empty")

            if (view.port.isEmpty())
                throw Exception("Port can not be empty")

            if (view.port.toUIntOrNull() == null)
                throw Exception("Port is expected to be positive number")
        } catch (e : Exception) {
            view.showValidationError(e)
            return false
        }
        return true
    }

    override fun onViewDetached() {
        deinitView()
    }
}