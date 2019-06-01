@file:Suppress("NOTHING_TO_INLINE")

package ru.mdashlw.kda.api.command

import ru.mdashlw.kda.api.builders.EmbedBuilder
import ru.mdashlw.kda.internal.command.CommandClient

interface ReplyModifier {
    fun check(command: Command, event: Command.Event): Boolean = true

    fun modify(command: Command, event: Command.Event): EmbedBuilder.() -> Unit
}

inline fun ReplyModifier.register() {
    CommandClient.INSTANCE.replyModifiers += this
}
