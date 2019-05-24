package ru.mdashlw.kda.builder.impl

import kotlinx.coroutines.asCoroutineDispatcher
import ru.mdashlw.kda.builder.Builder
import ru.mdashlw.kda.command.client.CommandClient
import ru.mdashlw.kda.command.colors.Colors
import ru.mdashlw.kda.command.colors.impl.DefaultColors
import ru.mdashlw.kda.command.emotes.Emotes
import ru.mdashlw.kda.command.emotes.impl.DefaultEmotes
import ru.mdashlw.kda.command.exceptionhandler.uncaught.UncaughtExceptionHandler
import ru.mdashlw.kda.command.exceptionhandler.uncaught.impl.DefaultUncaughtExceptionHandler
import ru.mdashlw.kda.command.guildsettings.provider.GuildSettingsProvider
import ru.mdashlw.kda.command.replies.Replies
import ru.mdashlw.kda.command.replies.impl.DefaultReplies
import ru.mdashlw.util.thread.CustomizableThreadFactory
import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class CommandClientBuilder : Builder<CommandClient>() {
    var owner: Long = 0L
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
            owner,
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
