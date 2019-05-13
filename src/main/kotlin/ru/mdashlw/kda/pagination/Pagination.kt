package ru.mdashlw.kda.pagination

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException
import ru.mdashlw.kda.builder.impl.EmbedBuilder
import ru.mdashlw.kda.embed
import ru.mdashlw.kda.extensions.wait
import java.time.Duration

class Pagination<T>(
    private val api: JDA,
    private val channelId: Long,
    private val usersIds: Set<Long>,
    itemsOnPage: Int,
    private val timeout: Duration,
    content: Collection<T>,
    private val embed: EmbedBuilder.(Collection<T>) -> Unit
) {
    private val chunked = content.chunked(itemsOnPage)
    private var page: Int = 0
    private var messageId: Long = 0

    private inline val total: Int
        get() = chunked.size

    private val channel: TextChannel
        get() = api.getTextChannelById(channelId)
            ?: throw IllegalStateException("Could not find the pagination text channel with ID $channelId")

    private val message: Message
        get() = channel.retrieveMessageById(messageId).complete()

    fun display() {
        if (total == 1) {
            channel.sendMessage(generateEmbed()).queue()
            return
        } else {
            channel.sendMessage(generateEmbed()).queue {
                messageId = it.idLong

                it.addReaction(ARROW_LEFT).queue()
                it.addReaction(ARROW_RIGHT).queue()
            }
        }

        api.wait(
            MessageReactionAddEvent::class,
            timeout = timeout,
            onCancel = {
                removeReaction(ARROW_LEFT, api.selfUser)
                removeReaction(ARROW_RIGHT, api.selfUser)
            },
            predicate = {
                it.channel == channel && it.messageIdLong == message.idLong && it.user.idLong in usersIds
            }
        ) {
            when (it.reactionEmote.name) {
                ARROW_LEFT -> {
                    if (canPaginate(page - 1)) {
                        paginate(page - 1)

                        try {
                            it.reaction.removeReaction(it.user).queue()
                        } catch (exception: InsufficientPermissionException) {
                        }
                    }
                }
                ARROW_RIGHT -> {
                    if (canPaginate(page + 1)) {
                        paginate(page + 1)

                        try {
                            it.reaction.removeReaction(it.user).queue()
                        } catch (exception: InsufficientPermissionException) {
                        }
                    }
                }
            }
        }
    }

    private fun generateEmbed(): MessageEmbed = embed {
        footer {
            text = "Page ${page + 1} / $total"
        }

        embed(chunked[page])
    }

    private fun canPaginate(page: Int): Boolean = page in 0 until total

    private fun paginate(page: Int) {
        this.page = page

        message.editMessage(generateEmbed()).queue()
    }

    private fun removeReaction(reaction: String, user: User) {
        try {
            message.reactions.find { it.reactionEmote.name == reaction }?.removeReaction(user)?.queue()
        } catch (exception: InsufficientPermissionException) {
        }
    }

    companion object {
        const val ARROW_LEFT = "\u2B05"
        const val ARROW_RIGHT = "\u27A1"
    }
}
