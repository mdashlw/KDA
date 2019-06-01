package ru.mdashlw.kda.api.utils

import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.requests.restaction.MessageAction
import ru.mdashlw.kda.api.build
import ru.mdashlw.kda.api.builders.EmbedBuilder

inline fun MessageChannel.send(content: String? = null, block: EmbedBuilder.() -> Unit): MessageAction =
    sendMessage(build(EmbedBuilder(), block)).content(content)
