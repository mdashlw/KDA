package ru.mdashlw.kda.builder.impl

import net.dv8tion.jda.api.JDA
import ru.mdashlw.kda.builder.Builder
import ru.mdashlw.kda.pagination.Pagination
import java.time.Duration

class PaginationBuilder<T>(private val content: Collection<T>) : Builder<Pagination<T>>() {
    lateinit var api: JDA
    var channelId: Long = 0
    lateinit var userIds: Set<Long>
    var itemsOnPage: Int = 25
    var timeout: Duration = Duration.ofMinutes(10)
    lateinit var embed: EmbedBuilder.(Collection<T>) -> Unit

    fun embed(block: EmbedBuilder.(Collection<T>) -> Unit) {
        embed = block
    }

    override fun build(): Pagination<T> =
        Pagination(
            api,
            channelId,
            userIds,
            itemsOnPage,
            timeout,
            content,
            embed
        )
}
