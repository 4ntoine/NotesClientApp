package name.antonsmirnov.notes.presenter.addnote

import name.antonsmirnov.notes.presenter.Note
import name.antonsmirnov.notes.usecase.AddNote

/**
 * MVP : Model
 */
class Model(
    private val useCase: AddNote,
    val note: Note) {

    sealed class State {
        override fun toString(): String = this.javaClass.simpleName

        /**
         * Initial state
         */
        object Initial : State()

        /**
         * Execution in progress
         */
        object Executing : State()

        /**
         * Successfully executed
         */
        object Executed : State()

        /**
         * Execution error
         */
        data class ExecutionError(val error: Exception) : State()
    }

    var state: State = State.Initial
        private set(newValue) {
            field = newValue
            presenter?.onModelChanged()
        }

    fun addNote() {
        state = State.Executing
        try {
            val response = useCase.execute(AddNote.Request(note.title, note.body))
            note.id = response.id
            state = State.Executed
        } catch (error : Exception) {
            state = State.ExecutionError(error)
        }
    }

    var presenter: Presenter? = null

    fun stateCopy(): Model {
        val copy = Model(this.useCase, this.note)
        copy.state = this.state
        return copy
    }
}