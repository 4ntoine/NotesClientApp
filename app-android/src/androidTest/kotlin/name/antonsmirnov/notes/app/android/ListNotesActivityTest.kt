package name.antonsmirnov.notes.app.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.SmallTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import name.antonsmirnov.notes.app.android.ui.ListNotesActivity
import name.antonsmirnov.notes.app.controller.rest.RestApi
import name.antonsmirnov.notes.usecase.ListNotes
import org.junit.*
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
@SmallTest
class ListNotesActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<ListNotesActivity> = ActivityTestRule(ListNotesActivity::class.java)

    companion object {
        private const val text1 = "Title1"
        private const val body1 = "Body1"

        @BeforeClass
        @JvmStatic
        fun setUpRest() {
            val response = Response.success(ListNotes.Response(listOf(ListNotes.Note("1", text1, body1))))

            val call = mock<Call<ListNotes.Response>>()
            whenever(call.execute()).thenReturn(response)

            val restApi = mock<RestApi>()
            whenever(restApi.listNotes()).thenReturn(call)

            RestApi.instance = restApi
        }
    }

    @Test
    fun testNoteIsShownInRecyclerView() {
        onView(withText(text1)).check(matches(isDisplayed()))
        onView(withText(body1)).check(matches(isDisplayed()))
    }
}