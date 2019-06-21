package name.antonsmirnov.notes.presenter.serverurl

/**
 * MVP : Model
 */
open class Model(
    _host: String? = null,
    _port: UInt? = null) {

    // we could add presenter notification on both `host` and `port` changed,
    // but we need it to be notified once only when both are set.
    // so the solution is to hide setters and introduce single method `update` instead.
    // TODO: better solution (introduce begin-/endUpdate() and skip change notification in-between)?

    var host: String? = _host
        private set

    var port: UInt? = _port
        private set

    // here arguments can't be null as not it's full validated by Presenter model
    open fun update(host: String, port: UInt) {
        this.host = host
        this.port = port
        presenter?.onModelChanged()
    }

    var presenter: Presenter? = null
}