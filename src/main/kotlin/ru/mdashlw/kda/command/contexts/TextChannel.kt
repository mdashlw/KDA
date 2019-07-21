package ru.mdashlw.kda.command.contexts

import net.dv8tion.jda.api.entities.TextChannel
import ru.mdashlw.kda.command.Command

fun Command.Context.optionalTextChannel(): TextChannel? {
    val arg = optionalWord() ?: return null

    return message.mentionedChannels.elementAtOrNull(index)
        ?: jda.getTextChannelsByName(arg, true).firstOrNull()
        ?: arg.toLongOrNull()?.let(jda::getTextChannelById)
        ?: error("Text channel `#$arg` does not exist.")
}

fun Command.Context.textChannel(fallback: TextChannel? = null): TextChannel =
    optionalTextChannel() ?: fallback ?: throw Command.Help()
