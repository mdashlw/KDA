package ru.mdashlw.kda.command.context.impl

import net.dv8tion.jda.api.entities.TextChannel
import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.context.CommandContext
import kotlin.reflect.KParameter

object TextChannelContext : CommandContext<TextChannel>() {
    override fun resolve(
        parameter: KParameter,
        event: Command.Event,
        index: Int,
        text: String,
        arg: String
    ): Result<TextChannel> =
        event.message.mentionedChannels.getOrNull(index)?.map()
            ?: event.guild.getTextChannelsByName(arg, true).firstOrNull()?.map()
            ?: arg.toLongOrNull()?.let { event.guild.getTextChannelById(it) }?.map()
            ?: throw Error(event.localize("contexts.textchannel.unknown", arg))
}
