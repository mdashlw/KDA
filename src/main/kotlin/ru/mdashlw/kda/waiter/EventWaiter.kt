package ru.mdashlw.kda.waiter

import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.EventListener
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import kotlin.reflect.KClass

object EventWaiter : EventListener {
    val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    val events = mutableMapOf<KClass<out GenericEvent>, WaitingEvent<GenericEvent>>()

    override fun onEvent(event: GenericEvent) {
        val waitingEvent = events[event::class] ?: return

        if (!waitingEvent.predicate(event)) {
            return
        }

        waitingEvent.action(event)
        waitingEvent.called += 1

        if (waitingEvent.amount >= waitingEvent.called) {
            waitingEvent.unregister()
        }
    }
}
