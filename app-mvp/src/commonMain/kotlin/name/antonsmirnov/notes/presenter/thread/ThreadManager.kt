package name.antonsmirnov.notes.presenter.thread

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
