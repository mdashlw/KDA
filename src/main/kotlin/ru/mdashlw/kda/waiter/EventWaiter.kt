package ru.mdashlw.kda.waiter

import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.EventListener
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import kotlin.reflect.KClass

object EventWaiter : EventListener {
    val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

    val events = mutableMapOf<KClass<out GenericEvent>, ArrayList<WaitingEvent<GenericEvent>>>()

    override fun onEvent(event: GenericEvent) {
        val waitingEvents = events[event::class] ?: return

        waitingEvents
            .filter { it.predicate(event) }
            .forEach {
                it.action(event)
                it.called += 1

                if (it.amount >= it.called) {
                    it.unregister()
                }
            }
    }
}
