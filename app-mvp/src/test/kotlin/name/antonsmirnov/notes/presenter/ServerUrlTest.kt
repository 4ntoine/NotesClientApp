package name.antonsmirnov.notes.presenter

import com.nhaarman.mockitokotlin2.*
import name.antonsmirnov.notes.presenter.serverurl.Model
import name.antonsmirnov.notes.presenter.serverurl.Presenter
import name.antonsmirnov.notes.presenter.serverurl.PresenterImpl
import name.antonsmirnov.notes.presenter.serverurl.View
import org.junit.Test
import kotlin.test.*

class ServerUrlTest {

    private val host = "localhost"
    private val port = "8080"

    private lateinit var model: Model
    private lateinit var presenter: Presenter
    private lateinit var view: View

    @BeforeTest
    fun setUp() {
        model = Model()
        presenter = PresenterImpl(model)
        view = mock()
        presenter.attachView(view)
    }

    @AfterTest
    fun tearDown() {
        presenter.onViewDetached()
    }

    @Test
    fun presenterValidatesEmptyHost() {
        whenever(view.host).thenReturn("")

        // see view state change visually in console
        whenever(view.showValidationError(any())).then { println(it.arguments[0]) }

        presenter.onViewChanged()
        verify(view).showValidationError(any())
    }

    @Test
    fun presenterValidatesEmptyPort() {
        whenever(view.host).thenReturn(host)
        whenever(view.port).thenReturn("")

        // see view state change visually in console
        whenever(view.showValidationError(any())).then { println(it.arguments[0]) }

        presenter.onViewChanged()
        verify(view).showValidationError(any())
    }

    @Test
    fun presenterValidatesNonPositivePort() {
        whenever(view.host).thenReturn(host)
        whenever(view.port).thenReturn("-1")

        // see view state change visually in console
        whenever(view.showValidationError(any())).then { println(it.arguments[0]) }

        presenter.onViewChanged()
        verify(view).showValidationError(any())
    }

    @Test
    fun presenterUpdatesModel() {
        whenever(view.host).thenReturn(host)
        whenever(view.port).thenReturn(port)

        // see view state change visually in console
        whenever(view.showValidationError(any())).then { println(it) }

        presenter.onViewChanged()
        verify(view, times(0)).showValidationError(any())
        verify(view, times(1)).showNotesList()

        assertEquals(host, model.host)
        assertEquals(port, model.port.toString())
    }
}