package ru.mdashlw.kda.api.builders

import kotlinx.coroutines.asCoroutineDispatcher
import ru.mdashlw.kda.api.Builder
import ru.mdashlw.kda.api.command.*
import ru.mdashlw.kda.internal.command.CommandClient
import ru.mdashlw.kda.internal.command.colors.DefaultColors
import ru.mdashlw.kda.internal.command.emotes.DefaultEmotes
import ru.mdashlw.kda.internal.command.exceptionhandlers.DefaultUncaughtExceptionHandler
import ru.mdashlw.kda.internal.command.replies.DefaultReplies
import ru.mdashlw.kda.internal.dsl.KdaDslMarker
import ru.mdashlw.util.thread.CustomizableThreadFactory
import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

@KdaDslMarker
class CommandClientBuilder : Builder<CommandClient>() {
    var ownerId: Long = 0L
    lateinit var prefix: String
    var languages: List<Locale> = listOf(Locale.ENGLISH)
    var requiresEmbedLinks: Boolean = true
    var executor: CoroutineContext =
        Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            CustomizableThreadFactory("KDA CommandHandler %d")
        ).asCoroutineDispatcher()
    var guildSettingsProvider: GuildSettingsProvider? = null
    var uncaughtExceptionHandler: UncaughtExceptionHandler = DefaultUncaughtExceptionHandler
    var replies: Replies = DefaultReplies
    var colors: Colors = DefaultColors
    var emotes: Emotes = DefaultEmotes

    inline fun replies(block: Replies.() -> Unit) {
        replies = replies.apply(block)
    }

    inline fun colors(block: Colors.() -> Unit) {
        colors = colors.apply(block)
    }

    inline fun emotes(block: Emotes.() -> Unit) {
        emotes = emotes.apply(block)
    }

    override fun build(): CommandClient =
        CommandClient(
            ownerId,
            prefix,
            languages,
            requiresEmbedLinks,
            executor,
            guildSettingsProvider,
            uncaughtExceptionHandler,
            replies,
            colors,
            emotes
        )
}
