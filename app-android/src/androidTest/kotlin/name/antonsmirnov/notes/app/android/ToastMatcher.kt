package name.antonsmirnov.notes.app.android

import android.view.WindowManager
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class ToastMatcher : TypeSafeMatcher<Root>() {
    override fun describeTo(description: Description) {
        description.appendText("is toast")
    }

    override fun matchesSafely(root: Root): Boolean {
        val type = root.getWindowLayoutParams().get().type
        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
            val windowToken = root.getDecorView().getWindowToken()
            val appToken = root.getDecorView().getApplicationWindowToken()
            if (windowToken === appToken) {
                // means this window isn't contained by any other windows.
                return true
            }
        }
        return false
    }
}