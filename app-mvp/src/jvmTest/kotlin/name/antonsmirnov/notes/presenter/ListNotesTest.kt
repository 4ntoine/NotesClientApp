package name.antonsmirnov.notes.presenter

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.TestCoroutineDispatcher
import name.antonsmirnov.notes.presenter.listnotes.ListNotesModel
import name.antonsmirnov.notes.presenter.listnotes.ListNotesPresenter
import name.antonsmirnov.notes.presenter.listnotes.ListNotesPresenterImpl
import name.antonsmirnov.notes.presenter.listnotes.ListNotesView
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
    private lateinit var model: ListNotesModel
    private lateinit var presenter: ListNotesPresenter
    private lateinit var view: ListNotesView

    @BeforeTest
    fun setUp() {
        useCase = mock()
        model = ListNotesModel(useCase)
        presenter = ListNotesPresenterImpl(model, TestCoroutineDispatcher())
        view = mock()

        // see view state change visually in console
        whenever(view.updateView(any())).then { println((it.arguments[0] as ListNotesModel).state) }

        presenter.attachView(view)
    }

    @AfterTest
    fun tearDown() {
        presenter.onViewDetached()
        model.presenter = null
    }

    @Test
    fun modelStateIsSetToLoaded() = runBlockingTest {
        whenever(useCase.execute()).thenReturn(ListNotes.Response(useCaseNotes))

        assertTrue(model.state is ListNotesModel.State.Initial)

        model.listNotes()

        assertTrue(model.state is ListNotesModel.State.Loaded)
        assertEquals(notes, (model.state as ListNotesModel.State.Loaded).notes)
    }

    @Test
    fun modelStateIsSetToError() = runBlockingTest {
        val error = Exception("No internet connection")
        /* can't use thenThrow because of following:
            org.mockito.exceptions.base.MockitoException:
            Checked exception is invalid for this method!
        */
        whenever(useCase.execute()).thenAnswer { throw error }

        assertTrue(model.state is ListNotesModel.State.Initial)

        model.listNotes()

        assertTrue(model.state is ListNotesModel.State.LoadError)
        assertEquals(error, (model.state as ListNotesModel.State.LoadError).error)
    }

    @Test
    fun modelNotifiesStateChanged() = runBlockingTest {
        whenever(useCase.execute()).thenReturn(ListNotes.Response(useCaseNotes))

        val _presenter = mock<ListNotesPresenter>()
        model.presenter = _presenter

        // see view state change visually in console
        whenever(_presenter.onModelChanged()).then { println(it) }

        assertTrue(model.state is ListNotesModel.State.Initial)

        model.listNotes()

        verify(_presenter, times(2 /* Loading + Loaded */)).onModelChanged()
    }

    @Test
    fun presenterListNotesActuallyStartsLoading() = runBlockingTest {
        whenever(useCase.execute()).thenReturn(ListNotes.Response(useCaseNotes))

        assertTrue(model.state is ListNotesModel.State.Initial)

        presenter.listNotes()

        assertTrue(model.state is ListNotesModel.State.Loaded)
        verify(view, times(3 /* Initial + Loading + Loaded */)).updateView(any())
    }
}