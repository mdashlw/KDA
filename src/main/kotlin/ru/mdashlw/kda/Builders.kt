package ru.mdashlw.kda

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import ru.mdashlw.kda.builder.Builder
import ru.mdashlw.kda.builder.impl.EmbedBuilder
import ru.mdashlw.kda.builder.impl.KdaBuilder
import ru.mdashlw.kda.builder.impl.PaginationBuilder
import ru.mdashlw.kda.pagination.Pagination

@PublishedApi
internal inline fun <B : Builder<T>, T> build(builder: B, block: B.() -> Unit): T =
    builder.apply(block).build()

inline fun kda(block: KdaBuilder.() -> Unit): JDA = build(KdaBuilder(), block)

inline fun embed(block: EmbedBuilder.() -> Unit): MessageEmbed = build(EmbedBuilder(), block)

inline fun <T> pagination(content: Collection<T>, block: PaginationBuilder<T>.() -> Unit): Pagination<T> =
    build(PaginationBuilder(content), block)
