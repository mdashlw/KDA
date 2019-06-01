package ru.mdashlw.kda.api.utils

import net.dv8tion.jda.api.events.GenericEvent
import ru.mdashlw.kda.api.waiter.WaitingEvent
import java.time.Duration

inline fun <reified T : GenericEvent> waitFor(
    amount: Int = 1,
    timeout: Duration = Duration.ZERO,
    noinline onCancel: () -> Unit = {},
    noinline predicate: (T) -> Boolean = { true },
    noinline action: (T) -> Unit
) {
    WaitingEvent(T::class, amount, timeout, onCancel, predicate, action).register()
}
