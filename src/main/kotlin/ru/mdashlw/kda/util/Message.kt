package ru.mdashlw.kda.util

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.requests.restaction.MessageAction
import ru.mdashlw.kda.build
import ru.mdashlw.kda.builder.impl.EmbedBuilder

inline fun Message.edit(content: String? = null, block: EmbedBuilder.() -> Unit): MessageAction =
    editMessage(build(EmbedBuilder(), block)).content(content)
