package ru.mdashlw.kda.pagination

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException
import net.dv8tion.jda.api.requests.RestAction
import ru.mdashlw.kda.builder.impl.EmbedBuilder
import ru.mdashlw.kda.embed
import ru.mdashlw.kda.extensions.wait
import java.time.Duration

class Pagination<T>(
    private val api: JDA,
    private val channelId: Long,
    private val usersIds: Set<Long>,
    private val timeout: Duration,
    itemsOnPage: Int,
    content: Collection<T>,
    private val embed: EmbedBuilder.(Collection<T>) -> Unit
) {
    private val chunked: List<List<T>> = content.chunked(itemsOnPage)

    private var page: Int = 0

    private var messageId: Long = 0

    private val total: Int
        get() = chunked.size

    private val channel: TextChannel
        get() = api.getTextChannelById(channelId)
            ?: throw IllegalStateException("Could not find the pagination text channel with ID $channelId")

    private val message: RestAction<Message>
        get() = channel.retrieveMessageById(messageId)

    fun display() {
        val channel = channel

        if (total == 1) {
            channel.sendMessage(generateEmbed()).queue()
            return
        }

        channel.sendMessage(generateEmbed()).queue {
            messageId = it.idLong

            it.addReaction(ARROW_LEFT).queue()
            it.addReaction(ARROW_RIGHT).queue()
        }

        api.wait<MessageReactionAddEvent>(
            0,
            timeout,
            onCancel = {
                message.queue {
                    it.reactions
                        .filter {
                            val emote = it.reactionEmote.name

                            emote == ARROW_LEFT || emote == ARROW_RIGHT
                        }
                        .forEach {
                            try {
                                it.removeReaction(api.selfUser).queue()
                            } catch (exception: InsufficientPermissionException) {
                            }
                        }
                }
            },
            predicate = {
                it.channel.idLong == channelId
                        && it.messageIdLong == messageId
                        && it.user.idLong in usersIds
            }
        ) {
            val emote = it.reactionEmote.name

            if (emote == ARROW_LEFT || emote == ARROW_RIGHT) {
                try {
                    it.reaction.removeReaction(it.user).queue()
                } catch (exception: InsufficientPermissionException) {
                }
            }

            val newPage = when (emote) {
                ARROW_LEFT -> page - 1
                ARROW_RIGHT -> page + 1
                else -> 0
            }

            if (canPaginate(newPage)) {
                paginate(newPage)
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

        message.queue {
            it.editMessage(generateEmbed()).queue()
        }
    }

    companion object {
        const val ARROW_LEFT = "\u2B05"
        const val ARROW_RIGHT = "\u27A1"
    }
}
