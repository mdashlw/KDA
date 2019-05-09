package ru.mdashlw.kda.waiter

import net.dv8tion.jda.api.events.GenericEvent
import java.time.Duration
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

class WaitingEvent<T : GenericEvent>(
    private val target: KClass<T>,
    val amount: Int,
    private val timeout: Duration,
    val onCancel: () -> Unit,
    val predicate: (T) -> Boolean,
    val action: (T) -> Unit
) {
    var called: Int = 0
    private var timeoutJob: ScheduledFuture<*>? = null

    @Suppress("UNCHECKED_CAST")
    fun register() {
        EventWaiter.events[target] = this as WaitingEvent<GenericEvent>

        if (!timeout.isZero) {
            timeoutJob = EventWaiter.executor.schedule(
                { unregister() },
                timeout.seconds,
                TimeUnit.SECONDS
            )
        }
    }

    fun unregister() {
        EventWaiter.events -= target
        timeoutJob?.cancel(false)
        onCancel()
    }
}
