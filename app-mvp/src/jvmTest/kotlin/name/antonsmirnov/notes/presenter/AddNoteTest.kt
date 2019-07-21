package name.antonsmirnov.notes.presenter

import com.nhaarman.mockitokotlin2.*
import name.antonsmirnov.notes.presenter.addnote.AddNoteModel
import name.antonsmirnov.notes.presenter.addnote.AddNotePresenter
import name.antonsmirnov.notes.presenter.addnote.AddNotePresenterImpl
import name.antonsmirnov.notes.presenter.addnote.AddNoteView
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
    private lateinit var model: AddNoteModel
    private lateinit var presenter: AddNotePresenter
    private lateinit var view: AddNoteView

    @BeforeTest
    fun setUp() {
        note = Note(null, "", null)
        useCase = mock()
        model = AddNoteModel(useCase, note)
        presenter = AddNotePresenterImpl(model, BlockingThreadManager())
        view = mock()

        // see view state change visually in console
        whenever(view.updateView(any())).then { println((it.arguments[0] as AddNoteModel).state) }

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

        assertTrue(model.state is AddNoteModel.State.Initial)

        model.addNote()

        assertTrue(model.state is AddNoteModel.State.Executed)
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

        assertTrue(model.state is AddNoteModel.State.Initial)

        model.addNote()

        assertTrue(model.state is AddNoteModel.State.ExecutionError)
        assertEquals(error, (model.state as AddNoteModel.State.ExecutionError).error)
    }

    @Test
    fun modelNotifiesStateChanged() {
        whenever(useCase.execute(any())).thenReturn(AddNote.Response(id))

        val _presenter = mock<AddNotePresenter>()
        model.presenter = _presenter

        // see view state change visually in console
        whenever(_presenter.onModelChanged()).then { println(it) }

        assertTrue(model.state is AddNoteModel.State.Initial)

        model.addNote()

        verify(_presenter, times(2 /* Executing + Executed */)).onModelChanged()
    }

    @Test
    fun presenterAddNoteActuallyStartsAdding() {
        whenever(useCase.execute(any())).thenReturn(AddNote.Response(id))

        assertTrue(model.state is AddNoteModel.State.Initial)

        presenter.addNote()

        assertTrue(model.state is AddNoteModel.State.Executed)
        verify(view, times(3 /* Initial + Executing + Executed */)).updateView(any())
    }

    @Test
    fun presenterUpdatesModelFromView() {
        whenever(useCase.execute(any())).thenReturn(AddNote.Response(id))
        val newTitle = "new title"
        val newBody = "new body"
        whenever(view.updateModel(any())).then {
            val modelArgument = it.getArgument<AddNoteModel>(0)
            modelArgument.note.title = newTitle
            modelArgument.note.body = newBody
            this
        }

        assertTrue(model.state is AddNoteModel.State.Initial)

        presenter.onViewChanged()
        verify(view, times(1)).updateModel(model)

        assertTrue(model.state is AddNoteModel.State.Executed)
        assertEquals(newTitle, model.note.title)
        assertEquals(newBody, model.note.body)
        verify(view, times(3 /* Initial + Executing + Executed */)).updateView(any())
    }
}