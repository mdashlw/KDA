@file:Suppress("NOTHING_TO_INLINE")

package ru.mdashlw.kda.extensions

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.EventListener

inline fun <reified T : GenericEvent> JDA.listenTo(crossinline action: (event: T) -> Unit) {
    addEventListener(
        object : EventListener {
            override fun onEvent(event: GenericEvent) {
                if (event is T) {
                    action(event)
                }
            }
        }
    )
}
