package ru.mdashlw.kda.api.builders

import net.dv8tion.jda.api.JDA
import ru.mdashlw.kda.api.Builder
import ru.mdashlw.kda.api.Pagination
import java.time.Duration

class PaginationBuilder<T>(private val content: Collection<T>) : Builder<Pagination<T>>() {
    lateinit var api: JDA
    var channelId: Long = 0
    lateinit var userIds: Set<Long>
    var timeout: Duration = Duration.ofMinutes(10)
    var itemsPerPage: Int = 15
    var displayFooter: Boolean = true
    lateinit var embed: EmbedBuilder.(Collection<T>) -> Unit

    fun embed(block: EmbedBuilder.(Collection<T>) -> Unit) {
        embed = block
    }

    override fun build(): Pagination<T> =
        Pagination(
            api,
            channelId,
            userIds,
            timeout,
            itemsPerPage,
            displayFooter,
            content,
            embed
        )
}
