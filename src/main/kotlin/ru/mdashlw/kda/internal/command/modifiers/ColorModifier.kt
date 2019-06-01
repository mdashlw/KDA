package ru.mdashlw.kda.internal.command.modifiers

import ru.mdashlw.kda.api.builders.EmbedBuilder
import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.ReplyModifier
import ru.mdashlw.kda.internal.command.CommandClient

object ColorModifier : ReplyModifier {
    override fun modify(command: Command, event: Command.Event): EmbedBuilder.() -> Unit = {
        CommandClient.INSTANCE.colors.default?.let { color = it }
    }
}
