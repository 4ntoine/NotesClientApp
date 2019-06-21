package name.antonsmirnov.notes.presenter.listnotes

import name.antonsmirnov.notes.presenter.Note
import name.antonsmirnov.notes.usecase.ListNotes

/**
 * MVP : Model
 */
class Model(
    private val useCase: ListNotes) {

    sealed class State {
        override fun toString(): String = this.javaClass.simpleName

        /**
         * Initial state
         */
        object Initial : State()

        /**
         * Loading in progress
         */
        object Loading : State()

        /**
         * Successfully loaded
         */
        data class Loaded(val notes: Collection<Note>) : State()

        /**
         * Error loading
         */
        data class LoadError(val error: Exception) : State()
    }

    var state: State = State.Initial
        private set(newValue) {
            field = newValue
            presenter?.onModelChanged()
        }

    fun listNotes() {
        state = State.Loading
        try {
            val response = useCase.execute()
            state = State.Loaded(response.notes.map { Note(it.id, it.title, it.body) })
        } catch (error : Exception) {
            state = State.LoadError(error)
        }
    }

    var presenter: Presenter? = null
}