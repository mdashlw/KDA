@file:Suppress("NOTHING_TO_INLINE")

package ru.mdashlw.kda.extensions

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent
import ru.mdashlw.kda.waiter.WaitingEvent
import java.time.Duration

inline fun <reified T : GenericEvent> JDA.wait(
    amount: Int = 1,
    timeout: Duration = Duration.ZERO,
    noinline onCancel: () -> Unit = {},
    noinline predicate: (T) -> Boolean = { true },
    noinline action: (T) -> Unit
) {
    WaitingEvent(T::class, amount, timeout, onCancel, predicate, action).register()
}
