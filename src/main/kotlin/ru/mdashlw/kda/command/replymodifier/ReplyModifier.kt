@file:Suppress("NOTHING_TO_INLINE")

package ru.mdashlw.kda.command.replymodifier

import ru.mdashlw.kda.builder.impl.EmbedBuilder
import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.client.CommandClient

interface ReplyModifier {
    fun check(command: Command, event: Command.Event): Boolean = true

    fun modify(command: Command, event: Command.Event): EmbedBuilder.() -> Unit
}

inline fun ReplyModifier.register() {
    CommandClient.INSTANCE.replyModifiers += this
}
