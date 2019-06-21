package name.antonsmirnov.notes.presenter

interface BasePresenter<ConcreteView> {
    fun onModelChanged()
    fun onViewChanged()

    fun attachView(view: ConcreteView)
    fun onViewDetached()
}