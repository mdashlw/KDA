package ru.mdashlw.kda.extensions

import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.requests.restaction.MessageAction
import ru.mdashlw.kda.build
import ru.mdashlw.kda.builder.impl.EmbedBuilder

inline fun MessageChannel.send(content: String? = null, block: EmbedBuilder.() -> Unit): MessageAction =
    sendMessage(build(EmbedBuilder(), block)).content(content)
