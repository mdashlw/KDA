package ru.mdashlw.kda.command.contexts

import net.dv8tion.jda.api.entities.TextChannel
import ru.mdashlw.kda.command.Command

fun Command.Context.nullableTextChannel(): TextChannel? {
    val arg = take() ?: return null

    return message.mentionedChannels.elementAtOrNull(index)
        ?: jda.getTextChannelsByName(arg, true).firstOrNull()
        ?: arg.toLongOrNull()?.let { jda.getTextChannelById(it) }
        ?: error("Text channel `#$arg` does not exist.")
}

fun Command.Context.textChannel(fallback: TextChannel? = null): TextChannel =
    nullableTextChannel() ?: fallback ?: throw Command.Help()
