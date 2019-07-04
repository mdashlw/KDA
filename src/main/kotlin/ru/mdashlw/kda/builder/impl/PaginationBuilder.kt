package ru.mdashlw.kda.builder.impl

import net.dv8tion.jda.api.JDA
import ru.mdashlw.kda.builder.Builder
import ru.mdashlw.kda.pagination.Pagination
import java.time.Duration

class PaginationBuilder<T>(private val content: Collection<T>) : Builder<Pagination<T>>() {
    lateinit var jda: JDA
    var channelId: Long = 0
    lateinit var userIds: Set<Long>
    var timeout: Duration = Duration.ofMinutes(10)
    var itemsPerPage: Int = 15
    var displayFooter: Boolean = true
    lateinit var provider: EmbedBuilder.(Collection<T>) -> Unit

    fun provider(block: EmbedBuilder.(Collection<T>) -> Unit) {
        provider = block
    }

    override fun build(): Pagination<T> =
        Pagination(
            content,
            jda,
            channelId,
            userIds,
            timeout,
            itemsPerPage,
            displayFooter,
            provider
        )
}
