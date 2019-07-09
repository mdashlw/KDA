package ru.mdashlw.kda.pagination

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException
import net.dv8tion.jda.api.requests.RestAction
import ru.mdashlw.kda.builder.impl.EmbedBuilder
import ru.mdashlw.kda.embed
import ru.mdashlw.kda.util.waitFor
import java.time.Duration

class Pagination<T>(
    content: Collection<T>,
    val jda: JDA,
    private val channelId: Long,
    private val userIds: Set<Long>,
    private val timeout: Duration,
    itemsPerPage: Int,
    private val displayFooter: Boolean,
    val provider: EmbedBuilder.(Collection<T>) -> Unit
) {
    private val chunks: List<List<T>> = content.chunked(itemsPerPage)
    private var page: Int = 0

    private var messageId: Long = 0

    private val total: Int
        get() = chunks.size

    private fun generateMessage(): MessageEmbed = embed {
        if (displayFooter) {
            footer {
                text = "Page ${page + 1} / $total"
            }
        }

        provider(chunks.getOrNull(page).orEmpty())
    }

    @Suppress("NAME_SHADOWING")
    private fun paginate(page: Int) {
        var page = page

        if (page < 0) {
            page = total - 1
        } else if (page >= total) {
            page = 0
        }

        this.page = page

        try {
            jda.getTextChannelById(channelId)?.retrieveMessageById(messageId)?.queue(
                { message ->
                    message.editMessage(generateMessage()).queue(
                        {},
                        {
                            if (it !is ErrorResponseException) {
                                RestAction.getDefaultFailure().accept(it)
                            }
                        }
                    )
                },
                {
                    if (it !is ErrorResponseException) {
                        RestAction.getDefaultFailure().accept(it)
                    }
                }
            )
        } catch (exception: InsufficientPermissionException) {
        }
    }

    fun init() {
        val channel = jda.getTextChannelById(channelId) ?: return
        val message = generateMessage()

        if (total <= 1) {
            try {
                channel.sendMessage(message).queue(
                    {},
                    {
                        if (it !is ErrorResponseException) {
                            RestAction.getDefaultFailure().accept(it)
                        }
                    }
                )
            } catch (exception: InsufficientPermissionException) {
            }
            return
        }

        try {
            channel.sendMessage(message).queue(
                {
                    messageId = it.idLong

                    it.addReaction(ARROW_LEFT).queue()
                    it.addReaction(ARROW_RIGHT).queue()
                },
                {
                    if (it !is ErrorResponseException) {
                        RestAction.getDefaultFailure().accept(it)
                    }
                }
            )
        } catch (exception: InsufficientPermissionException) {
        }

        waitFor<MessageReactionAddEvent>(
            amount = 0,
            timeout = timeout,
            onCancel = {
                try {
                    channel.retrieveMessageById(messageId).queue(
                        { message ->
                            message.reactions
                                .filter {
                                    val emote = it.reactionEmote.name

                                    emote == ARROW_LEFT || emote == ARROW_RIGHT
                                }
                                .forEach { reaction ->
                                    reaction.removeReaction().queue(
                                        {},
                                        {
                                            if (it !is ErrorResponseException) {
                                                RestAction.getDefaultFailure().accept(it)
                                            }
                                        }
                                    )
                                }
                        },
                        {
                            if (it !is ErrorResponseException) {
                                RestAction.getDefaultFailure().accept(it)
                            }
                        }
                    )
                } catch (exception: InsufficientPermissionException) {
                }
            },
            predicate = {
                it.channel.idLong == channelId &&
                        it.messageIdLong == messageId &&
                        it.user.idLong in userIds
            }
        ) { event ->
            val emote = event.reactionEmote.name

            if (emote != ARROW_LEFT && emote != ARROW_RIGHT) {
                return@waitFor
            }

            try {
                event.reaction.removeReaction(event.user).queue(
                    {},
                    {
                        if (it !is ErrorResponseException) {
                            RestAction.getDefaultFailure().accept(it)
                        }
                    }
                )
            } catch (exception: InsufficientPermissionException) {
            }

            val page = when (emote) {
                ARROW_LEFT -> page - 1
                ARROW_RIGHT -> page + 1
                else -> throw IllegalStateException()
            }

            paginate(page)
        }
    }

    companion object {
        const val ARROW_LEFT = "\u2B05"
        const val ARROW_RIGHT = "\u27A1"
    }
}
