package name.antonsmirnov.notes.app.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.SmallTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import name.antonsmirnov.notes.app.android.ui.ServerUrlActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ServerUrlActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<ServerUrlActivity> = ActivityTestRule(ServerUrlActivity::class.java)

    private val toastMatcher = ToastMatcher()

    @Before
    fun setUp() {
        // wait Toast from previous test to be hidden
        Thread.sleep(3 * 1000)
    }

    @Test
    fun testErrorMessageShownForEmptyHostField() {
        onView(withId(R.id.activity_serverurl_host))
                .perform(clearText(), closeSoftKeyboard())

        onView((withId(R.id.activity_serverurl_ok)))
                .perform(click())

        onView(withText("Host can not be empty"))
                .inRoot(toastMatcher)
                .check(matches(isDisplayed()))
    }

    @Test
    fun testErrorMessageShownForEmptyPortField() {
        onView(withId(R.id.activity_serverurl_host))
                .perform(clearText(), typeText("localhost"), closeSoftKeyboard())

        onView(withId(R.id.activity_serverurl_port))
                .perform(clearText(), closeSoftKeyboard())

        onView((withId(R.id.activity_serverurl_ok)))
                .perform(click())

        onView(withText("Port can not be empty"))
                .inRoot(toastMatcher)
                .check(matches(isDisplayed()))
    }

    @Test
    fun testActivityFinishedForValidFields() {
        onView(withId(R.id.activity_serverurl_host))
                .perform(clearText(), typeText("localhost"), closeSoftKeyboard())

        onView(withId(R.id.activity_serverurl_port))
                .perform(clearText(), typeText("8080"), closeSoftKeyboard())

        onView((withId(R.id.activity_serverurl_ok)))
                .perform(click())

        // this is shown in ListNotesActivity which is started after ServerUrlActivity is finished
        onView(withText("Failed to list the notes: Failed to connect to localhost/127.0.0.1:8080"))
                .inRoot(toastMatcher)
                .check(matches(isDisplayed()))
    }
}