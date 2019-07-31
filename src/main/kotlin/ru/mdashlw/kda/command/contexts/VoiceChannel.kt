package ru.mdashlw.kda.command.contexts

import net.dv8tion.jda.api.entities.VoiceChannel
import ru.mdashlw.kda.command.Command

fun Command.Context.optionalVoiceChannel(): VoiceChannel? {
    val arg = optionalWord() ?: return null

    return jda.getVoiceChannelsByName(arg, true).firstOrNull()
        ?: arg.toLongOrNull()?.let(jda::getVoiceChannelById)
        ?: error("Voice channel `$arg` does not exist.")
}

fun Command.Context.voiceChannel(fallback: VoiceChannel? = null): VoiceChannel =
    optionalVoiceChannel() ?: fallback ?: throw Command.Help()
