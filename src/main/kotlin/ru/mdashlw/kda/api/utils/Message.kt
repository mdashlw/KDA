package ru.mdashlw.kda.api.utils

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.requests.restaction.MessageAction
import ru.mdashlw.kda.api.build
import ru.mdashlw.kda.api.builders.EmbedBuilder

inline fun Message.edit(content: String? = null, block: EmbedBuilder.() -> Unit): MessageAction =
    editMessage(build(EmbedBuilder(), block)).content(content)
