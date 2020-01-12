package name.antonsmirnov.notes.presenter.listnotes

import name.antonsmirnov.notes.presenter.Note
import name.antonsmirnov.notes.usecase.ListNotes

/**
 * MVP : Model
 */
class ListNotesModel(
    private val useCase: ListNotes) {

    sealed class State {
        override fun toString(): String = this::class.simpleName!!

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

    suspend fun listNotes() {
        state = State.Loading
        try {
            val response = useCase.execute()
            state = State.Loaded(response.notes.map {
                // TODO: report and fix Kotlin/Native bug
                val note = Note(it.id, it.title, it.body)
                println("kotlin: $it >> $note")
                note
            })
//            println("kotlin: listNotes() " + state)
        } catch (error : Exception) {
            state = State.LoadError(error)
        }
    }

    var presenter: ListNotesPresenter? = null

    fun stateCopy(): ListNotesModel {
        val copy = ListNotesModel(this.useCase)
        copy.state = this.state
        return copy
    }
}