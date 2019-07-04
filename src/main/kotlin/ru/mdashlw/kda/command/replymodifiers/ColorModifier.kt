package ru.mdashlw.kda.command.replymodifiers

import ru.mdashlw.kda.builder.impl.EmbedBuilder
import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.ReplyModifier
import ru.mdashlw.kda.command.manager.CommandManager

object ColorModifier : ReplyModifier() {
    override fun modify(command: Command, context: Command.Context): EmbedBuilder.() -> Unit = {
        CommandManager.colors.default?.let { color = it }
    }
}
