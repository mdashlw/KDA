@file:Suppress("NOTHING_TO_INLINE")

package ru.mdashlw.kda.api.utils

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.hooks.EventListener

inline fun <reified T : GenericEvent> JDA.on(crossinline action: (event: T) -> Unit) {
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