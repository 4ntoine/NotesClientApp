package name.antonsmirnov.notes.presenter.coroutines

import kotlin.coroutines.*
import kotlinx.coroutines.*
import platform.darwin.*

class NsQueueDispatcher(
    val dispatchQueue: dispatch_queue_t
) : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatchQueue) {
            block.run()
        }
    }
}

actual fun getDefaultDispatcher(): CoroutineDispatcher = NsQueueDispatcher(dispatch_get_main_queue())