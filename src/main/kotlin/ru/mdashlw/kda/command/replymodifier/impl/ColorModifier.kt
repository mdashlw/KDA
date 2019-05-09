package ru.mdashlw.kda.command.replymodifier.impl

import ru.mdashlw.kda.builder.impl.EmbedBuilder
import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.client.CommandClient
import ru.mdashlw.kda.command.replymodifier.ReplyModifier

object ColorModifier : ReplyModifier {
    override fun modify(command: Command, event: Command.Event): EmbedBuilder.() -> Unit = {
        CommandClient.INSTANCE.colors.default?.let { color = it }
    }
}
