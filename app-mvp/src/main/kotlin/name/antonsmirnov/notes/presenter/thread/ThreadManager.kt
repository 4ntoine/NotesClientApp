package name.antonsmirnov.notes.presenter.thread

import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread

/**
 * Schedules long-running tasks for execution
 */
interface ThreadManager {
    fun run(block: () -> Unit)
    fun waitForFinished()
}

/**
 * Blocking implementation of ThreadManager
 * (blocks current thread)
 */
class BlockingThreadManager : ThreadManager {
    override fun run(block: () -> Unit) = block()

    override fun waitForFinished() {
        // nothing
    }
}

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