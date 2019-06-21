package name.antonsmirnov.notes.presenter

import com.nhaarman.mockitokotlin2.*
import name.antonsmirnov.notes.presenter.listnotes.Model
import name.antonsmirnov.notes.presenter.listnotes.Presenter
import name.antonsmirnov.notes.presenter.listnotes.PresenterImpl
import name.antonsmirnov.notes.presenter.listnotes.View
import name.antonsmirnov.notes.presenter.thread.BlockingThreadManager
import name.antonsmirnov.notes.usecase.ListNotes
import org.junit.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ListNotesTest {

    private val notes = arrayListOf(Note("1", "title", "body"))
    private val useCaseNotes: Collection<ListNotes.Note> = notes.map { ListNotes.Note(it.id, it.title, it.body) }

    private lateinit var useCase: ListNotes
    private lateinit var model: Model
    private lateinit var presenter: Presenter
    private lateinit var view: View

    @BeforeTest
    fun setUp() {
        useCase = mock()
        model = Model(useCase)
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
        whenever(useCase.execute()).thenReturn(ListNotes.Response(useCaseNotes))

        assertTrue(model.state is Model.State.Initial)

        model.listNotes()

        assertTrue(model.state is Model.State.Loaded)
        assertEquals(notes, (model.state as Model.State.Loaded).notes)
    }

    @Test
    fun modelStateIsSetToError() {
        val error = Exception("No internet connection")
        /* can't use thenThrow because of following:
            org.mockito.exceptions.base.MockitoException:
            Checked exception is invalid for this method!
        */
        whenever(useCase.execute()).thenAnswer { throw error }

        assertTrue(model.state is Model.State.Initial)

        model.listNotes()

        assertTrue(model.state is Model.State.LoadError)
        assertEquals(error, (model.state as Model.State.LoadError).error)
    }

    @Test
    fun modelNotifiesStateChanged() {
        whenever(useCase.execute()).thenReturn(ListNotes.Response(useCaseNotes))

        val _presenter = mock<Presenter>()
        model.presenter = _presenter

        // see view state change visually in console
        whenever(_presenter.onModelChanged()).then { println(it) }

        assertTrue(model.state is Model.State.Initial)

        model.listNotes()

        verify(_presenter, times(2 /* Loading + Loaded */)).onModelChanged()
    }

    @Test
    fun presenterListNotesActuallyStartsLoading() {
        whenever(useCase.execute()).thenReturn(ListNotes.Response(useCaseNotes))

        assertTrue(model.state is Model.State.Initial)

        presenter.listNotes()

        assertTrue(model.state is Model.State.Loaded)
        verify(view, times(3 /* Initial + Loading + Loaded */)).updateView(any())
    }
}