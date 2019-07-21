package name.antonsmirnov.notes.presenter

interface BaseView<ConcretePresenter> {
    var presenter: ConcretePresenter?
}