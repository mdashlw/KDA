@file:Suppress("NOTHING_TO_INLINE")

package ru.mdashlw.kda.extensions

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent
import ru.mdashlw.kda.waiter.WaitingEvent
import java.time.Duration
import kotlin.reflect.KClass

inline fun <T : GenericEvent> JDA.wait(
    target: KClass<T>,
    amount: Int = 0,
    timeout: Duration = Duration.ZERO,
    noinline onCancel: () -> Unit = {},
    noinline predicate: (T) -> Boolean = { true },
    noinline action: (T) -> Unit
) {
    WaitingEvent(target, amount, timeout, onCancel, predicate, action).register()
}
