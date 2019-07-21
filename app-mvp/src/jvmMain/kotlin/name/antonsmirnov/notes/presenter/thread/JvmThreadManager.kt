package name.antonsmirnov.notes.presenter.thread

import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

/**
 * Non-blocking implementation of ThreadManager
 * (creates new background thread every time)
 */
class BackgroundThreadManager : ThreadManager {
    var latch: CountDownLatch? = null

    override fun run(block: () -> Unit) {
        latch = CountDownLatch(1)
        thread {
            block()
            latch?.countDown()
        }
    }

    override fun waitForFinished() {
        latch?.let { it.await() }
    }
}