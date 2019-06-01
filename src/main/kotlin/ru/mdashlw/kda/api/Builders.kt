package ru.mdashlw.kda.api

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import ru.mdashlw.kda.api.builders.EmbedBuilder
import ru.mdashlw.kda.api.builders.JdaBuilder
import ru.mdashlw.kda.api.builders.PaginationBuilder

@PublishedApi
internal inline fun <B : Builder<T>, T> build(builder: B, block: B.() -> Unit): T =
    builder.apply(block).build()

inline fun jda(block: JdaBuilder.() -> Unit): JDA = build(JdaBuilder(), block)

inline fun embed(parent: MessageEmbed? = null, block: EmbedBuilder.() -> Unit): MessageEmbed =
    build(EmbedBuilder(parent), block)

inline fun <T> pagination(content: Collection<T>, block: PaginationBuilder<T>.() -> Unit): Pagination<T> =
    build(PaginationBuilder(content), block)
