package ru.mdashlw.kda.command.context.impl

import net.dv8tion.jda.api.entities.TextChannel
import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.context.CommandContext
import kotlin.reflect.KParameter

object TextChannelContext : CommandContext<TextChannel> {
    override fun resolve(
        parameter: KParameter,
        event: Command.Event,
        index: Int,
        text: String,
        arg: String
    ): CommandContext.Result<TextChannel> =
        event.message.mentionedChannels.getOrNull(index)?.let { CommandContext.Result(it) }
            ?: event.guild.getTextChannelsByName(arg, true).firstOrNull()?.let { CommandContext.Result(it) }
            ?: arg.toLongOrNull()?.let { event.guild.getTextChannelById(it) }?.let { CommandContext.Result(it) }
            ?: throw CommandContext.Error("Text channel #$arg does not exist.")
}
