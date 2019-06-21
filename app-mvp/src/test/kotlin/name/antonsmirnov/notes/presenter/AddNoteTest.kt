package name.antonsmirnov.notes.presenter

import com.nhaarman.mockitokotlin2.*
import name.antonsmirnov.notes.presenter.addnote.Model
import name.antonsmirnov.notes.presenter.addnote.Presenter
import name.antonsmirnov.notes.presenter.addnote.PresenterImpl
import name.antonsmirnov.notes.presenter.addnote.View
import name.antonsmirnov.notes.presenter.thread.BlockingThreadManager
import name.antonsmirnov.notes.usecase.AddNote
import org.junit.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddNoteTest {

    private val id = "1"

    private lateinit var note: Note
    private lateinit var useCase: AddNote
    private lateinit var model: Model
    private lateinit var presenter: Presenter
    private lateinit var view: View

    @BeforeTest
    fun setUp() {
        note = Note(null, "", null)
        useCase = mock()
        model = Model(useCase, note)
        presenter = PresenterImpl(model, BlockingThreadManager())
        view = mock()

        // see view state change visually in console
        whenever(view.updateView(any())).then { println((it.arguments[0] as Model).state) }

        presenter.attachView(view)
    }

    @AfterTest
    fun tearDown() {
        presenter.onViewDetached()
        model.presenter = null
    }

    @Test
    fun modelStateIsSetToLoaded() {
        whenever(useCase.execute(any())).thenReturn(AddNote.Response(id))

        assertTrue(model.state is Model.State.Initial)

        model.addNote()

        assertTrue(model.state is Model.State.Executed)
        assertEquals(id, model.note.id)
    }

    @Test
    fun modelStateIsSetToError() {
        val error = Exception("No internet connection")
        /* can't use thenThrow because of following:
            org.mockito.exceptions.base.MockitoException:
            Checked exception is invalid for this method!
        */
        whenever(useCase.execute(any())).thenAnswer { throw error }

        assertTrue(model.state is Model.State.Initial)

        model.addNote()

        assertTrue(model.state is Model.State.ExecutionError)
        assertEquals(error, (model.state as Model.State.ExecutionError).error)
    }

    @Test
    fun modelNotifiesStateChanged() {
        whenever(useCase.execute(any())).thenReturn(AddNote.Response(id))

        val _presenter = mock<Presenter>()
        model.presenter = _presenter

        // see view state change visually in console
        whenever(_presenter.onModelChanged()).then { println(it) }

        assertTrue(model.state is Model.State.Initial)

        model.addNote()

        verify(_presenter, times(2 /* Executing + Executed */)).onModelChanged()
    }

    @Test
    fun presenterAddNoteActuallyStartsAdding() {
        whenever(useCase.execute(any())).thenReturn(AddNote.Response(id))

        assertTrue(model.state is Model.State.Initial)

        presenter.addNote()

        assertTrue(model.state is Model.State.Executed)
        verify(view, times(3 /* Initial + Executing + Executed */)).updateView(any())
    }

    @Test
    fun presenterUpdatesModelFromView() {
        whenever(useCase.execute(any())).thenReturn(AddNote.Response(id))
        val newTitle = "new title"
        val newBody = "new body"
        whenever(view.updateModel(any())).then {
            val modelArgument = it.getArgument<Model>(0)
            modelArgument.note.title = newTitle
            modelArgument.note.body = newBody
            this
        }

        assertTrue(model.state is Model.State.Initial)

        presenter.onViewChanged()
        verify(view, times(1)).updateModel(model)

        assertTrue(model.state is Model.State.Executed)
        assertEquals(newTitle, model.note.title)
        assertEquals(newBody, model.note.body)
        verify(view, times(3 /* Initial + Executing + Executed */)).updateView(any())
    }
}